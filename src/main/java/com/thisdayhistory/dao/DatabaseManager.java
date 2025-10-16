package com.thisdayhistory.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import com.thisdayhistory.config.AppConfig;

public class DatabaseManager {
    private static final DatabaseManager instance = new DatabaseManager();
    private Connection connection;

    private DatabaseManager() {
        try {
            Properties props = AppConfig.loadConfig();
            String url = "jdbc:mysql://" + props.getProperty("db.host") + ":" + props.getProperty("db.port") + "/" + props.getProperty("db.name");
            this.connection = DriverManager.getConnection(url, props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (SQLException e) {
            e.printStackTrace();
            // In a real app, you'd want to handle this more gracefully
        }
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
