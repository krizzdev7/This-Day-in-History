package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AnimationUtil Class
 * Utility methods for creating smooth UI animations
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class AnimationUtil {

    /**
     * Private constructor to prevent instantiation
     */
    private AnimationUtil() {
    }

    /**
     * Fade in a component
     * 
     * @param component Component to fade in
     * @param duration Duration in milliseconds
     */
    public static void fadeIn(JComponent component, int duration) {
        component.setOpaque(false);
        
        final int steps = 20;
        final int delay = duration / steps;
        final float increment = 1.0f / steps;
        
        final float[] alpha = {0.0f};
        
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] += increment;
                if (alpha[0] >= 1.0f) {
                    alpha[0] = 1.0f;
                    timer.stop();
                }
                
                component.setOpaque(true);
                component.repaint();
                currentStep++;
            }
        });
        
        timer.start();
    }

    /**
     * Fade out a component
     * 
     * @param component Component to fade out
     * @param duration Duration in milliseconds
     * @param callback Runnable to execute after fade out
     */
    public static void fadeOut(JComponent component, int duration, Runnable callback) {
        final int steps = 20;
        final int delay = duration / steps;
        final float decrement = 1.0f / steps;
        
        final float[] alpha = {1.0f};
        
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] -= decrement;
                if (alpha[0] <= 0.0f) {
                    alpha[0] = 0.0f;
                    timer.stop();
                    if (callback != null) {
                        callback.run();
                    }
                }
                
                component.repaint();
                currentStep++;
            }
        });
        
        timer.start();
    }

    /**
     * Slide in a component from the left
     * 
     * @param component Component to slide in
     * @param duration Duration in milliseconds
     */
    public static void slideInFromLeft(JComponent component, int duration) {
        final int targetX = component.getX();
        final int startX = -component.getWidth();
        
        component.setLocation(startX, component.getY());
        
        animatePosition(component, startX, component.getY(), targetX, component.getY(), duration);
    }

    /**
     * Slide in a component from the right
     * 
     * @param component Component to slide in
     * @param duration Duration in milliseconds
     */
    public static void slideInFromRight(JComponent component, int duration) {
        Container parent = component.getParent();
        if (parent == null) return;
        
        final int targetX = component.getX();
        final int startX = parent.getWidth();
        
        component.setLocation(startX, component.getY());
        
        animatePosition(component, startX, component.getY(), targetX, component.getY(), duration);
    }

    /**
     * Slide in a component from the top
     * 
     * @param component Component to slide in
     * @param duration Duration in milliseconds
     */
    public static void slideInFromTop(JComponent component, int duration) {
        final int targetY = component.getY();
        final int startY = -component.getHeight();
        
        component.setLocation(component.getX(), startY);
        
        animatePosition(component, component.getX(), startY, component.getX(), targetY, duration);
    }

    /**
     * Slide in a component from the bottom
     * 
     * @param component Component to slide in
     * @param duration Duration in milliseconds
     */
    public static void slideInFromBottom(JComponent component, int duration) {
        Container parent = component.getParent();
        if (parent == null) return;
        
        final int targetY = component.getY();
        final int startY = parent.getHeight();
        
        component.setLocation(component.getX(), startY);
        
        animatePosition(component, component.getX(), startY, component.getX(), targetY, duration);
    }

    /**
     * Animate component position from start to end
     * 
     * @param component Component to animate
     * @param startX Start X position
     * @param startY Start Y position
     * @param endX End X position
     * @param endY End Y position
     * @param duration Duration in milliseconds
     */
    public static void animatePosition(JComponent component, int startX, int startY, 
                                      int endX, int endY, int duration) {
        final int steps = 30;
        final int delay = duration / steps;
        final int deltaX = (endX - startX) / steps;
        final int deltaY = (endY - startY) / steps;
        
        final int[] currentPos = {startX, startY};
        
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep++;
                
                if (currentStep >= steps) {
                    component.setLocation(endX, endY);
                    timer.stop();
                } else {
                    currentPos[0] += deltaX;
                    currentPos[1] += deltaY;
                    component.setLocation(currentPos[0], currentPos[1]);
                }
                
                component.getParent().repaint();
            }
        });
        
        timer.start();
    }

    /**
     * Pulse animation (grow and shrink)
     * 
     * @param component Component to pulse
     * @param scaleFactor Maximum scale factor (e.g., 1.1 for 110%)
     * @param duration Duration in milliseconds
     */
    public static void pulse(JComponent component, float scaleFactor, int duration) {
        final Dimension originalSize = component.getSize();
        final int targetWidth = (int)(originalSize.width * scaleFactor);
        final int targetHeight = (int)(originalSize.height * scaleFactor);
        
        final int steps = 20;
        final int delay = duration / (steps * 2);
        
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;
            private boolean growing = true;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep++;
                
                float progress = (float)currentStep / steps;
                int currentWidth, currentHeight;
                
                if (growing) {
                    currentWidth = (int)(originalSize.width + 
                                  (targetWidth - originalSize.width) * progress);
                    currentHeight = (int)(originalSize.height + 
                                   (targetHeight - originalSize.height) * progress);
                    
                    if (currentStep >= steps) {
                        growing = false;
                        currentStep = 0;
                    }
                } else {
                    currentWidth = (int)(targetWidth - 
                                  (targetWidth - originalSize.width) * progress);
                    currentHeight = (int)(targetHeight - 
                                   (targetHeight - originalSize.height) * progress);
                    
                    if (currentStep >= steps) {
                        component.setSize(originalSize);
                        timer.stop();
                        return;
                    }
                }
                
                component.setSize(currentWidth, currentHeight);
                component.getParent().repaint();
            }
        });
        
        timer.start();
    }

    /**
     * Shake animation (horizontal shake)
     * 
     * @param component Component to shake
     * @param intensity Shake intensity in pixels
     * @param duration Duration in milliseconds
     */
    public static void shake(JComponent component, int intensity, int duration) {
        final int originalX = component.getX();
        final int originalY = component.getY();
        
        final int steps = 10;
        final int delay = duration / steps;
        
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep++;
                
                if (currentStep >= steps) {
                    component.setLocation(originalX, originalY);
                    timer.stop();
                } else {
                    int offsetX = (int)((Math.random() - 0.5) * intensity * 2);
                    component.setLocation(originalX + offsetX, originalY);
                    component.getParent().repaint();
                }
            }
        });
        
        timer.start();
    }

    /**
     * Highlight animation (change background color temporarily)
     * 
     * @param component Component to highlight
     * @param highlightColor Highlight color
     * @param duration Duration in milliseconds
     */
    public static void highlight(JComponent component, Color highlightColor, int duration) {
        final Color originalColor = component.getBackground();
        
        component.setBackground(highlightColor);
        
        Timer timer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                component.setBackground(originalColor);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Rotate animation (for icons or small components)
     * Note: This is a simplified rotation that works best with square components
     * 
     * @param component Component to rotate
     * @param degrees Degrees to rotate (0-360)
     * @param duration Duration in milliseconds
     */
    public static void rotate(JComponent component, int degrees, int duration) {
        // This is a placeholder for rotation animation
        // Full implementation would require custom painting
        // For now, we'll just trigger a repaint
        component.repaint();
    }

    /**
     * Delayed action
     * 
     * @param action Action to perform
     * @param delay Delay in milliseconds
     */
    public static void delayedAction(Runnable action, int delay) {
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Smooth scroll to component
     * 
     * @param scrollPane JScrollPane containing the component
     * @param targetComponent Target component to scroll to
     * @param duration Duration in milliseconds
     */
    public static void smoothScrollTo(JScrollPane scrollPane, JComponent targetComponent, int duration) {
        Rectangle targetBounds = targetComponent.getBounds();
        JViewport viewport = scrollPane.getViewport();
        
        final Point startPos = viewport.getViewPosition();
        final Point endPos = new Point(targetBounds.x, targetBounds.y);
        
        final int steps = 30;
        final int delay = duration / steps;
        final int deltaX = (endPos.x - startPos.x) / steps;
        final int deltaY = (endPos.y - startPos.y) / steps;
        
        final int[] currentPos = {startPos.x, startPos.y};
        
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep++;
                
                if (currentStep >= steps) {
                    viewport.setViewPosition(endPos);
                    timer.stop();
                } else {
                    currentPos[0] += deltaX;
                    currentPos[1] += deltaY;
                    viewport.setViewPosition(new Point(currentPos[0], currentPos[1]));
                }
            }
        });
        
        timer.start();
    }
}
