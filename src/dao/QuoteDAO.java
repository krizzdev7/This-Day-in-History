package dao;

import db.DatabaseConnection;
import model.Quote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * QuoteDAO Class
 * Data Access Object for Quote entities
 * Handles all database operations related to quotes
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class QuoteDAO {
    private static final Logger LOGGER = Logger.getLogger(QuoteDAO.class.getName());
    private final DatabaseConnection dbConnection;
    private final Random random;

    /**
     * Constructor
     */
    public QuoteDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.random = new Random();
    }

    /**
     * Get a random quote from the database
     * 
     * @return Random quote or null if none available
     */
    public Quote getRandomQuote() {
        String sql = "SELECT * FROM quotes ORDER BY RAND() LIMIT 1";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                Quote quote = extractQuoteFromResultSet(rs);
                LOGGER.fine("Retrieved random quote: " + quote.getAuthor());
                return quote;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving random quote", e);
        }

        return null;
    }

    /**
     * Get all quotes
     * 
     * @return List of all quotes
     */
    public List<Quote> getAllQuotes() {
        List<Quote> quotes = new ArrayList<>();
        String sql = "SELECT * FROM quotes ORDER BY author";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                quotes.add(extractQuoteFromResultSet(rs));
            }

            LOGGER.info(String.format("Retrieved %d total quotes", quotes.size()));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all quotes", e);
        }

        return quotes;
    }

    /**
     * Get quotes by author
     * 
     * @param author Author name
     * @return List of quotes by that author
     */
    public List<Quote> getQuotesByAuthor(String author) {
        List<Quote> quotes = new ArrayList<>();
        String sql = "SELECT * FROM quotes WHERE author LIKE ? ORDER BY id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + author + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    quotes.add(extractQuoteFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Retrieved %d quotes for author: %s", quotes.size(), author));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving quotes by author", e);
        }

        return quotes;
    }

    /**
     * Search quotes by text content
     * 
     * @param keyword Keyword to search for
     * @return List of matching quotes
     */
    public List<Quote> searchQuotes(String keyword) {
        List<Quote> quotes = new ArrayList<>();
        String sql = "SELECT * FROM quotes WHERE text LIKE ? OR author LIKE ? ORDER BY author";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    quotes.add(extractQuoteFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Found %d quotes matching keyword: %s", quotes.size(), keyword));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching quotes", e);
        }

        return quotes;
    }

    /**
     * Get quote by ID
     * 
     * @param id Quote ID
     * @return Quote object or null if not found
     */
    public Quote getQuoteById(int id) {
        String sql = "SELECT * FROM quotes WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractQuoteFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving quote by ID", e);
        }

        return null;
    }

    /**
     * Get a random quote by a specific author
     * 
     * @param author Author name
     * @return Random quote by that author or null if none available
     */
    public Quote getRandomQuoteByAuthor(String author) {
        String sql = "SELECT * FROM quotes WHERE author LIKE ? ORDER BY RAND() LIMIT 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + author + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractQuoteFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving random quote by author", e);
        }

        return null;
    }

    /**
     * Insert a new quote
     * 
     * @param quote Quote to insert
     * @return Generated ID or -1 if failed
     */
    public int insertQuote(Quote quote) {
        String sql = "INSERT INTO quotes (author, text) VALUES (?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, quote.getAuthor());
            pstmt.setString(2, quote.getText());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        LOGGER.info("Inserted new quote with ID: " + id);
                        return id;
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting quote", e);
        }

        return -1;
    }

    /**
     * Update an existing quote
     * 
     * @param quote Quote to update
     * @return true if successful, false otherwise
     */
    public boolean updateQuote(Quote quote) {
        String sql = "UPDATE quotes SET author = ?, text = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, quote.getAuthor());
            pstmt.setString(2, quote.getText());
            pstmt.setInt(3, quote.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Updated quote with ID: " + quote.getId());
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating quote", e);
        }

        return false;
    }

    /**
     * Delete a quote
     * 
     * @param id Quote ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteQuote(int id) {
        String sql = "DELETE FROM quotes WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Deleted quote with ID: " + id);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting quote", e);
        }

        return false;
    }

    /**
     * Get all unique authors
     * 
     * @return List of author names
     */
    public List<String> getAllAuthors() {
        List<String> authors = new ArrayList<>();
        String sql = "SELECT DISTINCT author FROM quotes ORDER BY author";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                authors.add(rs.getString("author"));
            }

            LOGGER.info(String.format("Retrieved %d unique authors", authors.size()));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving authors", e);
        }

        return authors;
    }

    /**
     * Extract Quote object from ResultSet
     * 
     * @param rs ResultSet
     * @return Quote object
     * @throws SQLException if extraction fails
     */
    private Quote extractQuoteFromResultSet(ResultSet rs) throws SQLException {
        return new Quote(
            rs.getInt("id"),
            rs.getString("author"),
            rs.getString("text"),
            rs.getTimestamp("created_at")
        );
    }

    /**
     * Get count of quotes
     * 
     * @return Total number of quotes
     */
    public int getQuoteCount() {
        String sql = "SELECT COUNT(*) as count FROM quotes";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting quote count", e);
        }

        return 0;
    }
}
