package com.thisdayhistory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thisdayhistory.model.HistoricalFact;

public class HistoricalFactDAO {
    private static final Logger LOGGER = Logger.getLogger(HistoricalFactDAO.class.getName());
    private final DatabaseManager dbManager;

    public HistoricalFactDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public List<HistoricalFact> findByDate(int month, int day) {
        List<HistoricalFact> facts = new ArrayList<>();
        String sql = "SELECT * FROM historical_facts WHERE month = ? AND day = ? ORDER BY year";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, month);
            ps.setInt(2, day);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    facts.add(mapRowToHistoricalFact(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding facts by date: " + month + "-" + day, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return facts;
    }

    public List<HistoricalFact> search(String query) {
        List<HistoricalFact> facts = new ArrayList<>();
        String sql = "SELECT * FROM historical_facts WHERE MATCH(event) AGAINST (? IN BOOLEAN MODE) " +
                    "OR category LIKE ? ORDER BY month, day, year";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, query + "*");  // Use boolean mode for partial word matching
            ps.setString(2, "%" + query + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    facts.add(mapRowToHistoricalFact(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching facts with query: " + query, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return facts;
    }

    public HistoricalFact findById(int id) {
        String sql = "SELECT * FROM historical_facts WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToHistoricalFact(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding fact by id: " + id, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return null;
    }

    public int create(HistoricalFact fact) {
        String sql = "INSERT INTO historical_facts (month, day, year, event, category, source, is_favorite) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, fact.getMonth());
            ps.setInt(2, fact.getDay());
            ps.setObject(3, fact.getYear());
            ps.setString(4, fact.getEvent());
            ps.setString(5, fact.getCategory());
            ps.setString(6, fact.getSource());
            ps.setBoolean(7, fact.isFavorite());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating fact failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating fact failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating new fact: " + fact, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    public boolean update(HistoricalFact fact) {
        String sql = "UPDATE historical_facts SET month = ?, day = ?, year = ?, " +
                    "event = ?, category = ?, source = ?, is_favorite = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, fact.getMonth());
            ps.setInt(2, fact.getDay());
            ps.setObject(3, fact.getYear());
            ps.setString(4, fact.getEvent());
            ps.setString(5, fact.getCategory());
            ps.setString(6, fact.getSource());
            ps.setBoolean(7, fact.isFavorite());
            ps.setInt(8, fact.getId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating fact with id: " + fact.getId(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM historical_facts WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting fact with id: " + id, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    public List<HistoricalFact> findFavorites() {
        List<HistoricalFact> facts = new ArrayList<>();
        String sql = "SELECT * FROM historical_facts WHERE is_favorite = true ORDER BY month, day, year";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                facts.add(mapRowToHistoricalFact(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving favorite facts", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return facts;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM historical_facts ORDER BY category";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving categories", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return categories;
    }

    public List<HistoricalFact> findAll() {
        List<HistoricalFact> facts = new ArrayList<>();
        String sql = "SELECT * FROM historical_facts ORDER BY month, day, year";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                facts.add(mapRowToHistoricalFact(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all facts", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return facts;
    }

    private HistoricalFact mapRowToHistoricalFact(ResultSet rs) throws SQLException {
        HistoricalFact fact = new HistoricalFact();
        fact.setId(rs.getInt("id"));
        fact.setMonth(rs.getInt("month"));
        fact.setDay(rs.getInt("day"));
        fact.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
        fact.setEvent(rs.getString("event"));
        fact.setCategory(rs.getString("category"));
        fact.setSource(rs.getString("source"));
        fact.setFavorite(rs.getBoolean("is_favorite"));
        
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            fact.setCreatedAt(created.toLocalDateTime());
        }
        
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            fact.setUpdatedAt(updated.toLocalDateTime());
        }
        
        return fact;
    }
}
