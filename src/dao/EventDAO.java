package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Event;

/**
 * EventDAO Class
 * Data Access Object for Event entities
 * Handles all database operations related to historical events
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class EventDAO {
    private static final Logger LOGGER = Logger.getLogger(EventDAO.class.getName());
    private final DatabaseConnection dbConnection;

    /**
     * Constructor
     */
    public EventDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Get all events for a specific date (month and day)
     * 
     * @param month Month (1-12)
     * @param day Day of month (1-31)
     * @return List of events
     */
    public List<Event> getEventsByDate(int month, int day) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE month = ? AND day = ? ORDER BY year DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, day);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Retrieved %d events for %d/%d", events.size(), month, day));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving events by date", e);
        }

        return events;
    }

    /**
     * Get all events
     * 
     * @return List of all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY month, day, year DESC";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                events.add(extractEventFromResultSet(rs));
            }

            LOGGER.info(String.format("Retrieved %d total events", events.size()));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all events", e);
        }

        return events;
    }

    /**
     * Get events by category
     * 
     * @param category Category to filter by
     * @return List of events in that category
     */
    public List<Event> getEventsByCategory(String category) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE category = ? ORDER BY month, day, year DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Retrieved %d events for category: %s", events.size(), category));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving events by category", e);
        }

        return events;
    }

    /**
     * Search events by keyword in title or description
     * 
     * @param keyword Keyword to search for
     * @return List of matching events
     */
    public List<Event> searchEvents(String keyword) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE title LIKE ? OR description LIKE ? " +
                    "ORDER BY year DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Found %d events matching keyword: %s", events.size(), keyword));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching events", e);
        }

        return events;
    }

    /**
     * Get event by ID
     * 
     * @param id Event ID
     * @return Event object or null if not found
     */
    public Event getEventById(int id) {
        String sql = "SELECT * FROM events WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractEventFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving event by ID", e);
        }

        return null;
    }

    /**
     * Get all distinct categories
     * 
     * @return List of category names
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM events WHERE category IS NOT NULL ORDER BY category";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

            LOGGER.info(String.format("Retrieved %d categories", categories.size()));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving categories", e);
        }

        return categories;
    }

    /**
     * Get events for a date range
     * 
     * @param startMonth Start month
     * @param startDay Start day
     * @param endMonth End month
     * @param endDay End day
     * @return List of events in the date range
     */
    public List<Event> getEventsByDateRange(int startMonth, int startDay, int endMonth, int endDay) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE " +
                    "(month > ? OR (month = ? AND day >= ?)) AND " +
                    "(month < ? OR (month = ? AND day <= ?)) " +
                    "ORDER BY month, day, year DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, startMonth);
            pstmt.setInt(2, startMonth);
            pstmt.setInt(3, startDay);
            pstmt.setInt(4, endMonth);
            pstmt.setInt(5, endMonth);
            pstmt.setInt(6, endDay);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEventFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving events by date range", e);
        }

        return events;
    }

    /**
     * Insert a new event
     * 
     * @param event Event to insert
     * @return Generated ID or -1 if failed
     */
    public int insertEvent(Event event) {
        String sql = "INSERT INTO events (year, month, day, title, description, category, image_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, event.getYear());
            pstmt.setInt(2, event.getMonth());
            pstmt.setInt(3, event.getDay());
            pstmt.setString(4, event.getTitle());
            pstmt.setString(5, event.getDescription());
            pstmt.setString(6, event.getCategory());
            pstmt.setString(7, event.getImageUrl());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        LOGGER.info("Inserted new event with ID: " + id);
                        return id;
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting event", e);
        }

        return -1;
    }

    /**
     * Update an existing event
     * 
     * @param event Event to update
     * @return true if successful, false otherwise
     */
    public boolean updateEvent(Event event) {
        String sql = "UPDATE events SET year = ?, month = ?, day = ?, title = ?, " +
                    "description = ?, category = ?, image_url = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, event.getYear());
            pstmt.setInt(2, event.getMonth());
            pstmt.setInt(3, event.getDay());
            pstmt.setString(4, event.getTitle());
            pstmt.setString(5, event.getDescription());
            pstmt.setString(6, event.getCategory());
            pstmt.setString(7, event.getImageUrl());
            pstmt.setInt(8, event.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Updated event with ID: " + event.getId());
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating event", e);
        }

        return false;
    }

    /**
     * Delete an event
     * 
     * @param id Event ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteEvent(int id) {
        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Deleted event with ID: " + id);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting event", e);
        }

        return false;
    }

    /**
     * Extract Event object from ResultSet
     * 
     * @param rs ResultSet
     * @return Event object
     * @throws SQLException if extraction fails
     */
    private Event extractEventFromResultSet(ResultSet rs) throws SQLException {
        return new Event(
            rs.getInt("id"),
            rs.getInt("year"),
            rs.getInt("month"),
            rs.getInt("day"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("category"),
            rs.getString("image_url"),
            rs.getTimestamp("created_at")
        );
    }

    /**
     * Get count of events
     * 
     * @return Total number of events
     */
    public int getEventCount() {
        String sql = "SELECT COUNT(*) as count FROM events";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting event count", e);
        }

        return 0;
    }
}
