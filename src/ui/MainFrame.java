package ui;

import service.HistoryService;
import service.QuoteService;
import model.Event;
import model.Person;
import model.Quote;
import util.ImageLoader;
import util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * MainFrame Class
 * Main application window with all UI components
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class MainFrame extends JFrame {
    private static final Color BG_COLOR = new Color(245, 245, 220); // Beige
    private static final Color ACCENT_COLOR = new Color(184, 134, 11); // Dark Goldenrod
    
    private HistoryService historyService;
    private QuoteService quoteService;
    private LocalDate currentDate;
    
    private JPanel mainContentPanel;
    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;
    private FooterPanel footerPanel;
    private TimelinePanel timelinePanel;

    /**
     * Constructor
     */
    public MainFrame() {
        this.historyService = new HistoryService();
        this.quoteService = new QuoteService();
        this.currentDate = LocalDate.now();
        
        initUI();
        loadTodaysData();
    }

    /**
     * Initialize UI components
     */
    private void initUI() {
        setTitle("This Day in History");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(ImageLoader.loadAppIcon().getImage());
        
        // Set layout
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);
        
        // Create components
        headerPanel = new HeaderPanel(this);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area with sidebar
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        sidebarPanel = new SidebarPanel(this);
        centerPanel.add(sidebarPanel, BorderLayout.WEST);
        
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(BG_COLOR);
        mainContentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        footerPanel = new FooterPanel(quoteService);
        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Load today's historical data
     */
    public void loadTodaysData() {
        loadDataForDate(LocalDate.now());
    }

    /**
     * Load data for a specific date
     */
    public void loadDataForDate(LocalDate date) {
        this.currentDate = date;
        
        SwingUtilities.invokeLater(() -> {
            mainContentPanel.removeAll();
            
            // Create date header
            JPanel dateHeaderPanel = new JPanel(new BorderLayout());
            dateHeaderPanel.setOpaque(false);
            dateHeaderPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
            
            JLabel dateLabel = new JLabel(DateUtil.formatFullDate(date));
            dateLabel.setFont(new Font("Georgia", Font.BOLD, 32));
            dateLabel.setForeground(ACCENT_COLOR);
            
            JLabel dayLabel = new JLabel(DateUtil.getDayOfWeek(date));
            dayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            dayLabel.setForeground(new Color(100, 100, 100));
            
            dateHeaderPanel.add(dateLabel, BorderLayout.NORTH);
            dateHeaderPanel.add(dayLabel, BorderLayout.SOUTH);
            
            // Create tabbed pane for different sections
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            // Events tab
            JPanel eventsPanel = createEventsPanel(date);
            tabbedPane.addTab("Historical Events", eventsPanel);
            
            // Births tab
            JPanel birthsPanel = createBirthsPanel(date);
            tabbedPane.addTab("Famous Births", birthsPanel);
            
            // Deaths tab
            JPanel deathsPanel = createDeathsPanel(date);
            tabbedPane.addTab("Notable Deaths", deathsPanel);
            
            // Timeline tab
            timelinePanel = new TimelinePanel(historyService.getEventsForDate(date));
            tabbedPane.addTab("Timeline", timelinePanel);
            
            mainContentPanel.add(dateHeaderPanel, BorderLayout.NORTH);
            mainContentPanel.add(tabbedPane, BorderLayout.CENTER);
            
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        });
    }

    /**
     * Create events panel
     */
    private JPanel createEventsPanel(LocalDate date) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        List<Event> events = historyService.getEventsForDate(date);
        
        if (events.isEmpty()) {
            JLabel noDataLabel = new JLabel("No events found for this date.");
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            panel.add(noDataLabel);
        } else {
            for (Event event : events) {
                EventCard card = new EventCard(event);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(card);
                panel.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return containerPanel;
    }

    /**
     * Create births panel
     */
    private JPanel createBirthsPanel(LocalDate date) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        List<Person> people = historyService.getBirthsForDate(date);
        
        if (people.isEmpty()) {
            JLabel noDataLabel = new JLabel("No famous births found for this date.");
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            panel.add(noDataLabel);
        } else {
            for (Person person : people) {
                JPanel personCard = createPersonCard(person, true);
                personCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(personCard);
                panel.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return containerPanel;
    }

    /**
     * Create deaths panel
     */
    private JPanel createDeathsPanel(LocalDate date) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        List<Person> people = historyService.getDeathsForDate(date);
        
        if (people.isEmpty()) {
            JLabel noDataLabel = new JLabel("No notable deaths found for this date.");
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            panel.add(noDataLabel);
        } else {
            for (Person person : people) {
                JPanel personCard = createPersonCard(person, false);
                personCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(personCard);
                panel.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return containerPanel;
    }

    /**
     * Create person card
     */
    private JPanel createPersonCard(Person person, boolean isBirth) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(250, 235, 215));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Left: Lifespan
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel lifespanLabel = new JLabel(person.getLifespan());
        lifespanLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        lifespanLabel.setForeground(ACCENT_COLOR);
        lifespanLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        leftPanel.add(lifespanLabel);
        
        // Center: Details
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(person.getName());
        nameLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel categoryLabel = new JLabel(person.getCategory());
        categoryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        categoryLabel.setForeground(new Color(100, 100, 100));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(person.getShortDescription());
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        centerPanel.add(nameLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(categoryLabel);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(descArea);
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        
        return card;
    }

    /**
     * Search for events
     */
    public void searchEvents(String keyword) {
        SwingUtilities.invokeLater(() -> {
            mainContentPanel.removeAll();
            
            JLabel searchLabel = new JLabel("Search Results for: \"" + keyword + "\"");
            searchLabel.setFont(new Font("Georgia", Font.BOLD, 24));
            searchLabel.setForeground(ACCENT_COLOR);
            searchLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
            
            List<Event> events = historyService.searchEvents(keyword);
            
            JPanel resultsPanel = new JPanel();
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
            resultsPanel.setBackground(BG_COLOR);
            
            if (events.isEmpty()) {
                JLabel noResultsLabel = new JLabel("No events found matching your search.");
                noResultsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                resultsPanel.add(noResultsLabel);
            } else {
                for (Event event : events) {
                    EventCard card = new EventCard(event);
                    card.setAlignmentX(Component.LEFT_ALIGNMENT);
                    resultsPanel.add(card);
                    resultsPanel.add(Box.createVerticalStrut(10));
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(resultsPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            mainContentPanel.add(searchLabel, BorderLayout.NORTH);
            mainContentPanel.add(scrollPane, BorderLayout.CENTER);
            
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        });
    }

    /**
     * Filter by category
     */
    public void filterByCategory(String category) {
        SwingUtilities.invokeLater(() -> {
            mainContentPanel.removeAll();
            
            JLabel categoryLabel = new JLabel("Category: " + category);
            categoryLabel.setFont(new Font("Georgia", Font.BOLD, 24));
            categoryLabel.setForeground(ACCENT_COLOR);
            categoryLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
            
            List<Event> events = historyService.getEventsByCategory(category);
            
            JPanel resultsPanel = new JPanel();
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
            resultsPanel.setBackground(BG_COLOR);
            
            for (Event event : events) {
                EventCard card = new EventCard(event);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultsPanel.add(card);
                resultsPanel.add(Box.createVerticalStrut(10));
            }
            
            JScrollPane scrollPane = new JScrollPane(resultsPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            mainContentPanel.add(categoryLabel, BorderLayout.NORTH);
            mainContentPanel.add(scrollPane, BorderLayout.CENTER);
            
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        });
    }

    /**
     * Get history service
     */
    public HistoryService getHistoryService() {
        return historyService;
    }

    /**
     * Get current date
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }
}
