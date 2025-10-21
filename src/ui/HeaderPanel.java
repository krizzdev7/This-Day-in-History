package ui;

import util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * HeaderPanel Class
 * Header with logo, date picker, and navigation
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class HeaderPanel extends JPanel {
    private static final Color HEADER_BG = new Color(184, 134, 11);
    private static final Color HEADER_FG = Color.WHITE;
    
    private MainFrame mainFrame;
    private JComboBox<Integer> dayCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JTextField searchField;

    public HeaderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(HEADER_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Left: Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("📜");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel titleLabel = new JLabel("This Day in History");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(HEADER_FG);
        
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(titleLabel);
        
        // Center: Date picker
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setOpaque(false);
        
        LocalDate today = LocalDate.now();
        
        // Month combo
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(today.getMonthValue() - 1);
        
        // Day combo
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) days[i] = i + 1;
        dayCombo = new JComboBox<>(days);
        dayCombo.setSelectedIndex(today.getDayOfMonth() - 1);
        
        // Year combo
        Integer[] years = new Integer[150];
        int currentYear = today.getYear();
        for (int i = 0; i < 150; i++) years[i] = currentYear - i;
        yearCombo = new JComboBox<>(years);
        
        JButton goButton = new JButton("Go");
        goButton.addActionListener(e -> updateDate());
        
        JButton todayButton = new JButton("Today");
        todayButton.addActionListener(e -> {
            monthCombo.setSelectedIndex(today.getMonthValue() - 1);
            dayCombo.setSelectedIndex(today.getDayOfMonth() - 1);
            yearCombo.setSelectedIndex(0);
            mainFrame.loadTodaysData();
        });
        
        centerPanel.add(new JLabel("Date:"));
        centerPanel.add(monthCombo);
        centerPanel.add(dayCombo);
        centerPanel.add(yearCombo);
        centerPanel.add(goButton);
        centerPanel.add(todayButton);
        
        // Right: Search
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        searchField = new JTextField(20);
        searchField.addActionListener(e -> performSearch());
        
        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchButton.addActionListener(e -> performSearch());
        
        rightPanel.add(searchField);
        rightPanel.add(searchButton);
        
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void updateDate() {
        try {
            int month = monthCombo.getSelectedIndex() + 1;
            int day = (Integer) dayCombo.getSelectedItem();
            int year = (Integer) yearCombo.getSelectedItem();
            
            LocalDate date = LocalDate.of(year, month, day);
            mainFrame.loadDataForDate(date);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date selected!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            mainFrame.searchEvents(keyword);
        }
    }
}
