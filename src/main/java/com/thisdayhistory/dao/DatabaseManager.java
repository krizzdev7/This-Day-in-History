package com.thisdayhistory.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thisdayhistory.config.AppConfig;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final DatabaseManager instance = new DatabaseManager();
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY = 1000; // 1 second

    private final List<Connection> connectionPool;
    private final int maxPoolSize;
    private final String url;
    private final String username;
    private final String password;

    private DatabaseManager() {
        Properties props = AppConfig.loadConfig();
        url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                props.getProperty("db.host"),
                props.getProperty("db.port"),
                props.getProperty("db.name"));
        username = props.getProperty("db.user");
        password = props.getProperty("db.password");
        maxPoolSize = AppConfig.getIntProperty("db.maxPoolSize", 10);
        connectionPool = new ArrayList<>();

        // Initialize the connection pool
        for (int i = 0; i < maxPoolSize; i++) {
            try {
                Connection conn = createConnection();
                connectionPool.add(conn);
                LOGGER.info("Added connection " + (i + 1) + " to the pool");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to create database connection", e);
            }
        }
    }

    private Connection createConnection() throws SQLException {
        int retries = 0;
        SQLException lastException = null;

        while (retries < MAX_RETRIES) {
            try {
                return DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                lastException = e;
                retries++;
                LOGGER.warning("Failed to create connection, attempt " + retries + " of " + MAX_RETRIES);
                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Connection attempt interrupted", ie);
                }
            }
        }

        throw new SQLException("Failed to create database connection after " + MAX_RETRIES + " attempts", lastException);
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        for (Connection conn : connectionPool) {
            if (isConnectionValid(conn)) {
                return conn;
            }
        }

        // If no valid connection is found, create a new one
        Connection newConn = createConnection();
        connectionPool.add(newConn);
        return newConn;
    }

    private boolean isConnectionValid(Connection conn) {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(1);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Connection validation failed", e);
            return false;
        }
    }

    public void closeAll() {
        synchronized (connectionPool) {
            for (Connection conn : connectionPool) {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing connection", e);
                }
            }
            connectionPool.clear();
        }
        LOGGER.info("All database connections closed");
    }
}
