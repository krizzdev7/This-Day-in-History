package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseConnection Class
 * Manages database connections using singleton pattern
 * Reads configuration from config.properties file
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static DatabaseConnection instance;
    private String url;
    private String username;
    private String password;
    private String driver;

    /**
     * Private constructor to enforce singleton pattern
     */
    private DatabaseConnection() {
        loadConfiguration();
    }

    /**
     * Get singleton instance of DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Load database configuration from config.properties
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        try {
            // Try to load from config.properties
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            fis.close();

            this.url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/history_db");
            this.username = props.getProperty("db.username", "root");
            this.password = props.getProperty("db.password", "");
            this.driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");

            // Load JDBC driver
            Class.forName(driver);
            LOGGER.info("Database configuration loaded successfully");

        } catch (IOException e) {
            LOGGER.warning("Could not load config.properties, using default values");
            // Use default values
            this.url = "jdbc:mysql://localhost:3306/history_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            this.username = "root";
            this.password = "";
            this.driver = "com.mysql.cj.jdbc.Driver";

            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", ex);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Get a new database connection
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            LOGGER.fine("Database connection established");
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
            throw e;
        }
    }

    /**
     * Test database connection
     * 
     * @return true if connection successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn.isValid(5);
            if (isValid) {
                LOGGER.info("Database connection test successful");
            } else {
                LOGGER.warning("Database connection test failed");
            }
            return isValid;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection test failed", e);
            return false;
        }
    }

    /**
     * Close a database connection
     * 
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                LOGGER.fine("Database connection closed");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }

    /**
     * Close multiple AutoCloseable resources (Connection, Statement, ResultSet)
     * 
     * @param resources Resources to close
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error closing resource", e);
                }
            }
        }
    }

    // Getters for configuration values
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getDriver() {
        return driver;
    }
}
