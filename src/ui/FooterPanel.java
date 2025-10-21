package ui;

import service.QuoteService;
import model.Quote;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * FooterPanel Class
 * Footer with quote of the day
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class FooterPanel extends JPanel {
    private static final Color FOOTER_BG = new Color(184, 134, 11);
    private static final Color FOOTER_FG = Color.WHITE;
    
    private QuoteService quoteService;
    private JLabel quoteLabel;
    private JLabel authorLabel;

    public FooterPanel(QuoteService quoteService) {
        this.quoteService = quoteService;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 5));
        setBackground(FOOTER_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Quote display
        JPanel quotePanel = new JPanel();
        quotePanel.setLayout(new BoxLayout(quotePanel, BoxLayout.Y_AXIS));
        quotePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("💬 Quote of the Day");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        titleLabel.setForeground(FOOTER_FG);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        quoteLabel = new JLabel();
        quoteLabel.setFont(new Font("Georgia", Font.ITALIC, 13));
        quoteLabel.setForeground(FOOTER_FG);
        quoteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        authorLabel = new JLabel();
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        authorLabel.setForeground(new Color(255, 255, 200));
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        quotePanel.add(titleLabel);
        quotePanel.add(Box.createVerticalStrut(5));
        quotePanel.add(quoteLabel);
        quotePanel.add(Box.createVerticalStrut(3));
        quotePanel.add(authorLabel);
        
        // Load quote
        loadQuote();
        
        // Refresh button
        JButton refreshButton = new JButton("↻");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        refreshButton.setToolTipText("Get another quote");
        refreshButton.addActionListener(e -> loadQuote());
        
        add(quotePanel, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.EAST);
    }

    private void loadQuote() {
        Quote quote = quoteService.refreshQuoteOfTheDay();
        if (quote != null) {
            String text = quote.getText();
            if (text.length() > 100) {
                text = text.substring(0, 100) + "...";
            }
            quoteLabel.setText("\"" + text + "\"");
            authorLabel.setText("— " + quote.getAuthor());
        }
    }
}
