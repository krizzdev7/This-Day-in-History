package util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ImageLoader Class
 * Utility for loading and caching images
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class ImageLoader {
    private static final Logger LOGGER = Logger.getLogger(ImageLoader.class.getName());
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();
    
    // Default placeholder image dimensions
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 200;

    /**
     * Private constructor to prevent instantiation
     */
    private ImageLoader() {
    }

    /**
     * Load an image from URL or local path
     * 
     * @param path Image path or URL
     * @return ImageIcon or null if failed
     */
    public static ImageIcon loadImage(String path) {
        if (path == null || path.isEmpty()) {
            return createPlaceholder(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        // Check cache first
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try {
            ImageIcon icon;
            
            // Try loading from URL
            if (path.startsWith("http://") || path.startsWith("https://")) {
                URL url = new URL(path);
                icon = new ImageIcon(url);
            } else {
                // Try loading from local file
                icon = new ImageIcon(path);
            }

            // Verify image was loaded
            if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                imageCache.put(path, icon);
                LOGGER.fine("Loaded image: " + path);
                return icon;
            } else {
                LOGGER.warning("Failed to load image: " + path);
                return createPlaceholder(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error loading image: " + path, e);
            return createPlaceholder(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    /**
     * Load and resize an image
     * 
     * @param path Image path or URL
     * @param width Target width
     * @param height Target height
     * @return Resized ImageIcon
     */
    public static ImageIcon loadResizedImage(String path, int width, int height) {
        ImageIcon icon = loadImage(path);
        if (icon != null) {
            return resizeIcon(icon, width, height);
        }
        return createPlaceholder(width, height);
    }

    /**
     * Resize an existing ImageIcon
     * 
     * @param icon ImageIcon to resize
     * @param width Target width
     * @param height Target height
     * @return Resized ImageIcon
     */
    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return createPlaceholder(width, height);
        }
        
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    /**
     * Create a placeholder image with text
     * 
     * @param width Image width
     * @param height Image height
     * @return Placeholder ImageIcon
     */
    public static ImageIcon createPlaceholder(int width, int height) {
        return createPlaceholder(width, height, "No Image");
    }

    /**
     * Create a placeholder image with custom text
     * 
     * @param width Image width
     * @param height Image height
     * @param text Text to display
     * @return Placeholder ImageIcon
     */
    public static ImageIcon createPlaceholder(int width, int height, String text) {
        // Create a buffered image
        Image img = new java.awt.image.BufferedImage(width, height, 
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(0, 0, width, height);
        
        // Draw border
        g2d.setColor(new Color(180, 180, 180));
        g2d.drawRect(0, 0, width - 1, height - 1);
        
        // Draw text
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height + textHeight) / 2 - fm.getDescent();
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        
        return new ImageIcon(img);
    }

    /**
     * Create a colored square icon (useful for category indicators)
     * 
     * @param size Icon size
     * @param color Icon color
     * @return Colored ImageIcon
     */
    public static ImageIcon createColoredIcon(int size, Color color) {
        Image img = new java.awt.image.BufferedImage(size, size, 
                    java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(color);
        g2d.fillRoundRect(0, 0, size, size, 5, 5);
        
        g2d.dispose();
        
        return new ImageIcon(img);
    }

    /**
     * Get category color
     * 
     * @param category Category name
     * @return Color for that category
     */
    public static Color getCategoryColor(String category) {
        if (category == null) {
            return new Color(100, 100, 100);
        }
        
        switch (category.toLowerCase()) {
            case "politics":
                return new Color(178, 34, 34); // Firebrick
            case "science":
                return new Color(30, 144, 255); // Dodger Blue
            case "technology":
                return new Color(50, 205, 50); // Lime Green
            case "arts":
                return new Color(138, 43, 226); // Blue Violet
            case "music":
                return new Color(255, 105, 180); // Hot Pink
            case "sports":
                return new Color(255, 140, 0); // Dark Orange
            case "military":
                return new Color(85, 107, 47); // Dark Olive Green
            case "space":
                return new Color(25, 25, 112); // Midnight Blue
            case "civil rights":
                return new Color(184, 134, 11); // Dark Goldenrod
            case "literature":
                return new Color(139, 69, 19); // Saddle Brown
            case "disaster":
                return new Color(220, 20, 60); // Crimson
            case "economics":
                return new Color(0, 128, 128); // Teal
            case "exploration":
                return new Color(210, 105, 30); // Chocolate
            default:
                return new Color(105, 105, 105); // Dim Gray
        }
    }

    /**
     * Create a category icon
     * 
     * @param category Category name
     * @param size Icon size
     * @return Category icon
     */
    public static ImageIcon createCategoryIcon(String category, int size) {
        Color color = getCategoryColor(category);
        return createColoredIcon(size, color);
    }

    /**
     * Clear image cache
     */
    public static void clearCache() {
        imageCache.clear();
        LOGGER.info("Image cache cleared");
    }

    /**
     * Get cache size
     * 
     * @return Number of cached images
     */
    public static int getCacheSize() {
        return imageCache.size();
    }

    /**
     * Load default app icon
     * 
     * @return App icon
     */
    public static ImageIcon loadAppIcon() {
        // Create a simple app icon with "H" for History
        int size = 64;
        Image img = new java.awt.image.BufferedImage(size, size, 
                    java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background circle
        g2d.setColor(new Color(184, 134, 11)); // Dark Goldenrod
        g2d.fillOval(0, 0, size, size);
        
        // Draw "H" letter
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Georgia", Font.BOLD, 40));
        FontMetrics fm = g2d.getFontMetrics();
        String letter = "H";
        int x = (size - fm.stringWidth(letter)) / 2;
        int y = (size + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(letter, x, y);
        
        g2d.dispose();
        
        return new ImageIcon(img);
    }
}
