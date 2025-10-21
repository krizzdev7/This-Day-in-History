package model;

import java.sql.Timestamp;

/**
 * Quote Model Class
 * Represents an inspirational or historical quote
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class Quote {
    private int id;
    private String author;
    private String text;
    private Timestamp createdAt;

    /**
     * Default constructor
     */
    public Quote() {
    }

    /**
     * Constructor with essential fields
     */
    public Quote(String author, String text) {
        this.author = author;
        this.text = text;
    }

    /**
     * Full constructor
     */
    public Quote(int id, String author, String text, Timestamp createdAt) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get formatted quote with author
     */
    public String getFormattedQuote() {
        return String.format("\"%s\"\n\n— %s", text, author);
    }

    /**
     * Get short text (first 50 characters)
     */
    public String getShortText() {
        if (text == null || text.length() <= 50) {
            return text;
        }
        return text.substring(0, 50) + "...";
    }

    /**
     * Check if quote is long (more than 100 characters)
     */
    public boolean isLongQuote() {
        return text != null && text.length() > 100;
    }

    @Override
    public String toString() {
        return String.format("Quote{id=%d, author='%s', text='%s'}", 
                           id, author, getShortText());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quote quote = (Quote) obj;
        return id == quote.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
