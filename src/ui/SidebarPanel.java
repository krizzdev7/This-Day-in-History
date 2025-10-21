package ui;

import service.HistoryService;
import model.Event;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * SidebarPanel Class
 * Sidebar with filters and random facts
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class SidebarPanel extends JPanel {
    private static final Color SIDEBAR_BG = new Color(230, 230, 210);
    private static final Color ACCENT_COLOR = new Color(184, 134, 11);
    
    private MainFrame mainFrame;

    public SidebarPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(SIDEBAR_BG);
        setBorder(new EmptyBorder(20, 15, 20, 15));
        setPreferredSize(new Dimension(250, 0));
        
        // Title
        JLabel titleLabel = new JLabel("Filters");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(15));
        
        // Categories
        JLabel categoriesLabel = new JLabel("Categories:");
        categoriesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoriesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(categoriesLabel);
        add(Box.createVerticalStrut(10));
        
        List<String> categories = mainFrame.getHistoryService().getEventCategories();
        for (String category : categories) {
            JButton catButton = new JButton(category);
            catButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            catButton.setMaximumSize(new Dimension(220, 30));
            catButton.setBackground(Color.WHITE);
            catButton.setFocusPainted(false);
            catButton.addActionListener(e -> mainFrame.filterByCategory(category));
            add(catButton);
            add(Box.createVerticalStrut(5));
        }
        
        add(Box.createVerticalStrut(20));
        
        // Random fact
        JLabel randomLabel = new JLabel("Did You Know?");
        randomLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        randomLabel.setForeground(ACCENT_COLOR);
        randomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(randomLabel);
        add(Box.createVerticalStrut(10));
        
        JTextArea factArea = new JTextArea();
        factArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        factArea.setLineWrap(true);
        factArea.setWrapStyleWord(true);
        factArea.setEditable(false);
        factArea.setOpaque(false);
        factArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        Event randomFact = mainFrame.getHistoryService().getRandomFact();
        if (randomFact != null) {
            factArea.setText(randomFact.getTitle() + " (" + randomFact.getYear() + ")");
        }
        
        add(factArea);
        
        add(Box.createVerticalGlue());
    }
}
