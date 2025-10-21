package ui;

import model.Event;
import util.DateUtil;
import util.ImageLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * EventCard Class
 * Displays a single historical event as a styled card
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class EventCard extends JPanel {
    private static final Color CARD_BG = new Color(250, 235, 215); // Antique White
    private static final Color CARD_BORDER = new Color(210, 180, 140); // Tan
    private static final Color TITLE_COLOR = new Color(44, 36, 22);
    private static final Color TEXT_COLOR = new Color(60, 60, 60);
    private static final Color CATEGORY_BG = new Color(184, 134, 11); // Dark Goldenrod
    
    private Event event;
    private boolean expanded = false;

    /**
     * Constructor
     * 
     * @param event Event to display
     */
    public EventCard(Event event) {
        this.event = event;
        initUI();
    }

    /**
     * Initialize UI components
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Left: Year badge
        JPanel leftPanel = createYearPanel();
        add(leftPanel, BorderLayout.WEST);
        
        // Center: Content
        JPanel centerPanel = createContentPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Right: Category icon
        JPanel rightPanel = createCategoryPanel();
        add(rightPanel, BorderLayout.EAST);
        
        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(255, 245, 225));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(CARD_BG);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                showEventDetails();
            }
        });
    }

    /**
     * Create year panel with year display
     */
    private JPanel createYearPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel yearLabel = new JLabel(String.valueOf(event.getYear()));
        yearLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        yearLabel.setForeground(CATEGORY_BG);
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel agoLabel = new JLabel(DateUtil.formatYearsAgo(event.getYear()));
        agoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        agoLabel.setForeground(TEXT_COLOR);
        agoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(yearLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(agoLabel);
        
        return panel;
    }

    /**
     * Create content panel with title and description
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel(event.getTitle());
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Date
        JLabel dateLabel = new JLabel(event.getFormattedDate());
        dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dateLabel.setForeground(TEXT_COLOR);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Description
        JTextArea descArea = new JTextArea(event.getShortDescription());
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setForeground(TEXT_COLOR);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(descArea);
        
        return panel;
    }

    /**
     * Create category panel with category badge
     */
    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        
        if (event.getCategory() != null) {
            JLabel categoryLabel = new JLabel(event.getCategory());
            categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            categoryLabel.setForeground(Color.WHITE);
            categoryLabel.setOpaque(true);
            categoryLabel.setBackground(ImageLoader.getCategoryColor(event.getCategory()));
            categoryLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
            
            panel.add(categoryLabel, BorderLayout.NORTH);
        }
        
        return panel;
    }

    /**
     * Show full event details in a dialog
     */
    private void showEventDetails() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Event Details", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CATEGORY_BG);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(event.getTitle());
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel dateLabel = new JLabel(event.getFormattedDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(dateLabel, BorderLayout.SOUTH);
        
        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);
        
        JTextArea descArea = new JTextArea(event.getDescription());
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setBorder(null);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Category info
        if (event.getCategory() != null) {
            JLabel catLabel = new JLabel("Category: " + event.getCategory());
            catLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            contentPanel.add(catLabel, BorderLayout.SOUTH);
        }
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }

    /**
     * Get the event displayed in this card
     */
    public Event getEvent() {
        return event;
    }
}
