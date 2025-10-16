package com.thisdayhistory.dao;

import com.thisdayhistory.model.HistoricalFact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricalFactDAO {

    private Connection connection;

    public HistoricalFactDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public List<HistoricalFact> findByDate(int month, int day) {
        List<HistoricalFact> facts = new ArrayList<>();
        String sql = "SELECT * FROM historical_facts WHERE month = ? AND day = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, day);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                facts.add(mapRowToHistoricalFact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facts;
    }

    public List<HistoricalFact> search(String query) {
        List<HistoricalFact> facts = new ArrayList<>();
        String sql = "SELECT * FROM historical_facts WHERE event LIKE ? OR category LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String searchQuery = "%" + query + "%";
            ps.setString(1, searchQuery);
            ps.setString(2, searchQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                facts.add(mapRowToHistoricalFact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facts;
    }

    public HistoricalFact findById(int id) {
        String sql = "SELECT * FROM historical_facts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToHistoricalFact(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int create(HistoricalFact fact) {
        String sql = "INSERT INTO historical_facts (month, day, year, event, category, source, favorite) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, fact.getMonth());
            ps.setInt(2, fact.getDay());
            ps.setObject(3, fact.getYear());
            ps.setString(4, fact.getEvent());
            ps.setString(5, fact.getCategory());
            ps.setString(6, fact.getSource());
            ps.setBoolean(7, fact.isFavorite());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean update(HistoricalFact fact) {
        String sql = "UPDATE historical_facts SET month = ?, day = ?, year = ?, event = ?, category = ?, source = ?, favorite = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, fact.getMonth());
            ps.setInt(2, fact.getDay());
            ps.setObject(3, fact.getYear());
            ps.setString(4, fact.getEvent());
            ps.setString(5, fact.getCategory());
            ps.setString(6, fact.getSource());
            ps.setBoolean(7, fact.isFavorite());
            ps.setInt(8, fact.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM historical_facts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HistoricalFact> findFavorites() {
        List<HistoricalFact> facts = new ArrayList<>();
        String sql = "SELECT * FROM historical_facts WHERE favorite = TRUE";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                facts.add(mapRowToHistoricalFact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facts;
    }

    private HistoricalFact mapRowToHistoricalFact(ResultSet rs) throws SQLException {
        return new HistoricalFact(
                rs.getInt("id"),
                rs.getInt("month"),
                rs.getInt("day"),
                (Integer) rs.getObject("year"),
                rs.getString("event"),
                rs.getString("category"),
                rs.getString("source"),
                rs.getBoolean("favorite"),
                rs.getTimestamp("created_at")
        );
    }
}
