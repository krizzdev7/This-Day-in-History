package com.thisdayhistory.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppConfig {
    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    private AppConfig() {
        // Private constructor to prevent instantiation
    }

    public static Properties loadConfig() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                if (input == null) {
                    throw new IOException("Unable to find " + CONFIG_FILE);
                }
                properties.load(input);
                LOGGER.info("Successfully loaded configuration from " + CONFIG_FILE);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error loading configuration file", e);
                throw new RuntimeException("Failed to load configuration", e);
            }
        }
        return properties;
    }

    public static String getProperty(String key) {
        Properties props = loadConfig();
        String value = props.getProperty(key);
        if (value == null) {
            LOGGER.warning("Property not found: " + key);
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        Properties props = loadConfig();
        return props.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid integer property " + key + ": " + value);
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }
}
