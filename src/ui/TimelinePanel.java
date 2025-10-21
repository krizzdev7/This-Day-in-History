package ui;

import model.Event;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;

/**
 * TimelinePanel Class
 * Visual timeline display of historical events
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class TimelinePanel extends JPanel {
    private static final Color BG_COLOR = new Color(245, 245, 220);
    private static final Color LINE_COLOR = new Color(184, 134, 11);
    private static final Color POINT_COLOR = new Color(178, 34, 34);
    
    private List<Event> events;

    public TimelinePanel(List<Event> events) {
        this.events = events;
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (events == null || events.isEmpty()) {
            g.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            g.setColor(Color.GRAY);
            g.drawString("No events to display", getWidth() / 2 - 70, getHeight() / 2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int margin = 50;
        int startX = margin;
        int endX = getWidth() - margin;
        int centerY = getHeight() / 2;
        
        // Draw timeline line
        g2d.setColor(LINE_COLOR);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(new Line2D.Double(startX, centerY, endX, centerY));
        
        // Draw events
        if (events.size() > 0) {
            int spacing = (endX - startX) / Math.max(events.size(), 1);
            
            for (int i = 0; i < events.size() && i < 10; i++) {
                Event event = events.get(i);
                int x = startX + (i * spacing);
                
                // Draw point
                g2d.setColor(POINT_COLOR);
                g2d.fillOval(x - 8, centerY - 8, 16, 16);
                
                // Draw year
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Georgia", Font.BOLD, 12));
                String year = String.valueOf(event.getYear());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(year);
                g2d.drawString(year, x - textWidth / 2, centerY - 20);
                
                // Draw title (abbreviated)
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                String title = event.getTitle();
                if (title.length() > 20) {
                    title = title.substring(0, 17) + "...";
                }
                textWidth = fm.stringWidth(title);
                g2d.drawString(title, x - textWidth / 2, centerY + 30);
            }
        }
    }
}
