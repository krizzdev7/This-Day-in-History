import ui.MainFrame;
import db.DatabaseConnection;

import javax.swing.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main Class
 * Entry point for This Day in History application
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Main method - Application entry point
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not set system look and feel", e);
        }

        // Test database connection
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        if (!dbConnection.testConnection()) {
            JOptionPane.showMessageDialog(null,
                "Failed to connect to database.\n" +
                "Please check your MySQL server and config.properties file.\n" +
                "Make sure:\n" +
                "1. MySQL server is running\n" +
                "2. Database 'history_db' exists\n" +
                "3. Username and password are correct in config.properties",
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE);
            
            LOGGER.severe("Database connection failed. Application will continue but data may not be available.");
        }

        // Launch application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                LOGGER.info("Application started successfully");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error launching application", e);
                JOptionPane.showMessageDialog(null,
                    "An error occurred while launching the application:\n" + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
