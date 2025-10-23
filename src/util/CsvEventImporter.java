package util;

import dao.EventDAO;
import db.DatabaseConnection;
import model.Event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CsvEventImporter
 * Simple CSV importer to load historical events into the MySQL database.
 *
 * Flexible header mapping supported. Recognized columns (case-insensitive):
 * - year
 * - month
 * - day
 * - title | event
 * - description (optional)
 * - category (optional)
 * - image_url (optional)
 * - source (optional; appended to description if description is empty)
 *
 * Usage:
 *   java -cp "bin;lib/mysql-connector-j-9.4.0.jar" util.CsvEventImporter [path-to-csv]
 * If no path is provided, it will try: history_events.csv, then historical_events.csv
 */
public class CsvEventImporter {
    private static final Logger LOGGER = Logger.getLogger(CsvEventImporter.class.getName());

    public static void main(String[] args) {
        String csvPath = null;
        if (args.length > 0) {
            csvPath = args[0];
        } else {
            // Try common defaults
            if (new File("history_events.csv").exists()) {
                csvPath = "history_events.csv";
            } else if (new File("historical_events.csv").exists()) {
                csvPath = "historical_events.csv";
            }
        }

        if (csvPath == null) {
            System.err.println("No CSV file provided and no default file found (history_events.csv or historical_events.csv).\n" +
                    "Usage: java util.CsvEventImporter <path-to-csv>");
            System.exit(1);
        }

        File file = new File(csvPath);
        if (!file.exists()) {
            System.err.println("CSV file not found: " + csvPath);
            System.exit(1);
        }

        LOGGER.info("Starting import from: " + file.getAbsolutePath());
        CsvEventImporter importer = new CsvEventImporter();
        try {
            ImportResult result = importer.importCsv(file);
            LOGGER.info(String.format("Import completed. Processed=%d, Inserted=%d, Updated=%d, Skipped=%d, Errors=%d",
                    result.processed, result.inserted, result.updated, result.skipped, result.errors));
            System.out.println("+---------------------------+");
            System.out.println("| CSV Import Summary        |");
            System.out.println("+---------------------------+");
            System.out.printf("Processed: %d\n", result.processed);
            System.out.printf("Inserted : %d\n", result.inserted);
            System.out.printf("Updated  : %d\n", result.updated);
            System.out.printf("Skipped  : %d\n", result.skipped);
            System.out.printf("Errors   : %d\n", result.errors);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Import failed", e);
            System.err.println("Import failed: " + e.getMessage());
            System.exit(2);
        }
    }

    public ImportResult importCsv(File csvFile) throws Exception {
        ImportResult result = new ImportResult();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IllegalArgumentException("CSV is empty: " + csvFile.getAbsolutePath());
            }

            List<String> headers = parseCsvLine(headerLine);
            Map<String, Integer> idx = mapHeaderIndexes(headers);
            validateRequiredColumns(idx);

            DatabaseConnection dbc = DatabaseConnection.getInstance();
            try (Connection conn = dbc.getConnection()) {
                conn.setAutoCommit(false);

                String selectSql = "SELECT id FROM events WHERE year=? AND month=? AND day=? AND title=?";
                String insertSql = "INSERT INTO events (year, month, day, title, description, category, image_url) VALUES (?,?,?,?,?,?,?)";
                String updateSql = "UPDATE events SET description=?, category=?, image_url=? WHERE id=?";

                try (PreparedStatement psSelect = conn.prepareStatement(selectSql);
                     PreparedStatement psInsert = conn.prepareStatement(insertSql);
                     PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {

                    String line;
                    int lineNo = 1; // already consumed header
                    while ((line = reader.readLine()) != null) {
                        lineNo++;
                        if (line.trim().isEmpty()) { continue; }

                        List<String> cols = parseCsvLine(line);
                        // Normalize to header size
                        while (cols.size() < headers.size()) cols.add("");

                        try {
                            Integer year = parseIntSafe(get(cols, idx, "year"));
                            Integer month = parseIntSafe(get(cols, idx, "month"));
                            Integer day = parseIntSafe(get(cols, idx, "day"));
                            String title = trimToNull(get(cols, idx, "title"));
                            if (title == null) title = trimToNull(get(cols, idx, "event"));
                            String description = trimToNull(get(cols, idx, "description"));
                            String category = trimToNull(get(cols, idx, "category"));
                            String imageUrl = trimToNull(get(cols, idx, "image_url"));
                            String source = trimToNull(get(cols, idx, "source"));

                            if (description == null && source != null) {
                                description = "Source: " + source;
                            }

                            // Basic validation
                            if (year == null || month == null || day == null || title == null) {
                                result.skipped++;
                                LOGGER.warning(String.format("Line %d skipped due to missing required fields", lineNo));
                                continue;
                            }

                            // Check if exists
                            int existingId = -1;
                            psSelect.setInt(1, year);
                            psSelect.setInt(2, month);
                            psSelect.setInt(3, day);
                            psSelect.setString(4, title);
                            try (ResultSet rs = psSelect.executeQuery()) {
                                if (rs.next()) {
                                    existingId = rs.getInt(1);
                                }
                            }

                            if (existingId == -1) {
                                // Insert
                                psInsert.setInt(1, year);
                                psInsert.setInt(2, month);
                                psInsert.setInt(3, day);
                                psInsert.setString(4, title);
                                psInsert.setString(5, description);
                                psInsert.setString(6, category);
                                psInsert.setString(7, imageUrl);
                                psInsert.addBatch();
                                result.inserted++;
                            } else {
                                // Update minimal fields
                                psUpdate.setString(1, description);
                                psUpdate.setString(2, category);
                                psUpdate.setString(3, imageUrl);
                                psUpdate.setInt(4, existingId);
                                psUpdate.addBatch();
                                result.updated++;
                            }

                            result.processed++;

                            // Periodic batching
                            if ((result.inserted + result.updated) % 500 == 0) {
                                psInsert.executeBatch();
                                psUpdate.executeBatch();
                                conn.commit();
                            }
                        } catch (Exception rowEx) {
                            result.errors++;
                            LOGGER.log(Level.WARNING, "Error processing line " + lineNo + ": " + rowEx.getMessage(), rowEx);
                        }
                    }

                    // Flush remaining batches
                    psInsert.executeBatch();
                    psUpdate.executeBatch();
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        }

        return result;
    }

    private static class ImportResult {
        int processed = 0;
        int inserted = 0;
        int updated = 0;
        int skipped = 0;
        int errors = 0;
    }

    private static Map<String, Integer> mapHeaderIndexes(List<String> headers) {
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            String h = headers.get(i).trim().toLowerCase(Locale.ROOT);
            switch (h) {
                case "yr":
                case "year": idx.put("year", i); break;
                case "mon":
                case "month": idx.put("month", i); break;
                case "day": idx.put("day", i); break;
                case "title": idx.put("title", i); break;
                case "event": idx.put("event", i); break;
                case "desc":
                case "description": idx.put("description", i); break;
                case "cat":
                case "category": idx.put("category", i); break;
                case "image":
                case "image_url": idx.put("image_url", i); break;
                case "source":
                case "source_url": idx.put("source", i); break;
                default:
                    // ignore other columns
            }
        }
        return idx;
    }

    private static void validateRequiredColumns(Map<String, Integer> idx) {
        List<String> missing = new ArrayList<>();
        if (!idx.containsKey("year")) missing.add("year");
        if (!idx.containsKey("month")) missing.add("month");
        if (!idx.containsKey("day")) missing.add("day");
        if (!idx.containsKey("title") && !idx.containsKey("event")) missing.add("title/event");
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("CSV missing required columns: " + String.join(", ", missing));
        }
    }

    private static String get(List<String> cols, Map<String, Integer> idx, String key) {
        Integer i = idx.get(key);
        if (i == null || i < 0 || i >= cols.size()) return null;
        return cols.get(i);
    }

    private static Integer parseIntSafe(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    // Basic CSV parser that supports quoted fields with embedded commas and double quotes
    private static List<String> parseCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    // lookahead for escaped quote
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        sb.append('"');
                        i++; // skip next quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    sb.append(c);
                }
            } else {
                if (c == ',') {
                    cols.add(sb.toString());
                    sb.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    sb.append(c);
                }
            }
        }
        cols.add(sb.toString());
        return cols;
    }
}
