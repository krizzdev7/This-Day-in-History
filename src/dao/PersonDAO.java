package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Person;

/**
 * PersonDAO Class
 * Data Access Object for Person entities
 * Handles all database operations related to famous people
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class PersonDAO {
    private static final Logger LOGGER = Logger.getLogger(PersonDAO.class.getName());
    private final DatabaseConnection dbConnection;

    /**
     * Constructor
     */
    public PersonDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Get all people born on a specific date
     * 
     * @param month Birth month (1-12)
     * @param day Birth day (1-31)
     * @return List of people
     */
    public List<Person> getPeopleBornOnDate(int month, int day) {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM people WHERE birth_month = ? AND birth_day = ? " +
                    "ORDER BY birth_year DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, day);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    people.add(extractPersonFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Retrieved %d people born on %d/%d", people.size(), month, day));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving people by birth date", e);
        }

        return people;
    }

    /**
     * Get all people who died on a specific date
     * 
     * @param month Death month (1-12)
     * @param day Death day (1-31)
     * @return List of people
     */
    public List<Person> getPeopleDiedOnDate(int month, int day) {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM people WHERE death_month = ? AND death_day = ? " +
                    "ORDER BY death_year DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, day);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    people.add(extractPersonFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Retrieved %d people who died on %d/%d", people.size(), month, day));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving people by death date", e);
        }

        return people;
    }

    /**
     * Get all people
     * 
     * @return List of all people
     */
    public List<Person> getAllPeople() {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM people ORDER BY name";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                people.add(extractPersonFromResultSet(rs));
            }

            LOGGER.info(String.format("Retrieved %d total people", people.size()));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all people", e);
        }

        return people;
    }

    /**
     * Get people by category
     * 
     * @param category Category to filter by
     * @return List of people in that category
     */
    public List<Person> getPeopleByCategory(String category) {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM people WHERE category = ? ORDER BY name";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    people.add(extractPersonFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Retrieved %d people for category: %s", people.size(), category));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving people by category", e);
        }

        return people;
    }

    /**
     * Search people by name or description
     * 
     * @param keyword Keyword to search for
     * @return List of matching people
     */
    public List<Person> searchPeople(String keyword) {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM people WHERE name LIKE ? OR description LIKE ? " +
                    "ORDER BY name";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    people.add(extractPersonFromResultSet(rs));
                }
            }

            LOGGER.info(String.format("Found %d people matching keyword: %s", people.size(), keyword));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching people", e);
        }

        return people;
    }

    /**
     * Get person by ID
     * 
     * @param id Person ID
     * @return Person object or null if not found
     */
    public Person getPersonById(int id) {
        String sql = "SELECT * FROM people WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPersonFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving person by ID", e);
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
        String sql = "SELECT DISTINCT category FROM people WHERE category IS NOT NULL ORDER BY category";

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
     * Get people who are still alive
     * 
     * @return List of living people
     */
    public List<Person> getLivingPeople() {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM people WHERE death_year IS NULL ORDER BY name";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                people.add(extractPersonFromResultSet(rs));
            }

            LOGGER.info(String.format("Retrieved %d living people", people.size()));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving living people", e);
        }

        return people;
    }

    /**
     * Insert a new person
     * 
     * @param person Person to insert
     * @return Generated ID or -1 if failed
     */
    public int insertPerson(Person person) {
        String sql = "INSERT INTO people (name, birth_year, death_year, birth_month, birth_day, " +
                    "death_month, death_day, category, description, image_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, person.getName());
            setIntOrNull(pstmt, 2, person.getBirthYear());
            setIntOrNull(pstmt, 3, person.getDeathYear());
            setIntOrNull(pstmt, 4, person.getBirthMonth());
            setIntOrNull(pstmt, 5, person.getBirthDay());
            setIntOrNull(pstmt, 6, person.getDeathMonth());
            setIntOrNull(pstmt, 7, person.getDeathDay());
            pstmt.setString(8, person.getCategory());
            pstmt.setString(9, person.getDescription());
            pstmt.setString(10, person.getImageUrl());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        LOGGER.info("Inserted new person with ID: " + id);
                        return id;
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting person", e);
        }

        return -1;
    }

    /**
     * Update an existing person
     * 
     * @param person Person to update
     * @return true if successful, false otherwise
     */
    public boolean updatePerson(Person person) {
        String sql = "UPDATE people SET name = ?, birth_year = ?, death_year = ?, " +
                    "birth_month = ?, birth_day = ?, death_month = ?, death_day = ?, " +
                    "category = ?, description = ?, image_url = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, person.getName());
            setIntOrNull(pstmt, 2, person.getBirthYear());
            setIntOrNull(pstmt, 3, person.getDeathYear());
            setIntOrNull(pstmt, 4, person.getBirthMonth());
            setIntOrNull(pstmt, 5, person.getBirthDay());
            setIntOrNull(pstmt, 6, person.getDeathMonth());
            setIntOrNull(pstmt, 7, person.getDeathDay());
            pstmt.setString(8, person.getCategory());
            pstmt.setString(9, person.getDescription());
            pstmt.setString(10, person.getImageUrl());
            pstmt.setInt(11, person.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Updated person with ID: " + person.getId());
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating person", e);
        }

        return false;
    }

    /**
     * Delete a person
     * 
     * @param id Person ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deletePerson(int id) {
        String sql = "DELETE FROM people WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Deleted person with ID: " + id);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting person", e);
        }

        return false;
    }

    /**
     * Helper method to set Integer or NULL in PreparedStatement
     */
    private void setIntOrNull(PreparedStatement pstmt, int index, Integer value) throws SQLException {
        if (value != null) {
            pstmt.setInt(index, value);
        } else {
            pstmt.setNull(index, Types.INTEGER);
        }
    }

    /**
     * Extract Person object from ResultSet
     * 
     * @param rs ResultSet
     * @return Person object
     * @throws SQLException if extraction fails
     */
    private Person extractPersonFromResultSet(ResultSet rs) throws SQLException {
        return new Person(
            rs.getInt("id"),
            rs.getString("name"),
            (Integer) rs.getObject("birth_year"),
            (Integer) rs.getObject("death_year"),
            (Integer) rs.getObject("birth_month"),
            (Integer) rs.getObject("birth_day"),
            (Integer) rs.getObject("death_month"),
            (Integer) rs.getObject("death_day"),
            rs.getString("category"),
            rs.getString("description"),
            rs.getString("image_url")
        );
    }

    /**
     * Get count of people
     * 
     * @return Total number of people
     */
    public int getPersonCount() {
        String sql = "SELECT COUNT(*) as count FROM people";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting person count", e);
        }

        return 0;
    }
}
