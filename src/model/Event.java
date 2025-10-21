package model;

import java.time.LocalDate;
import java.sql.Timestamp;

/**
 * Event Model Class
 * Represents a historical event with all its properties
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class Event {
    private int id;
    private int year;
    private int month;
    private int day;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private Timestamp createdAt;

    /**
     * Default constructor
     */
    public Event() {
    }

    /**
     * Constructor with all fields except id
     */
    public Event(int year, int month, int day, String title, String description, 
                 String category, String imageUrl) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    /**
     * Full constructor
     */
    public Event(int id, int year, int month, int day, String title, String description,
                 String category, String imageUrl, Timestamp createdAt) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the event date as LocalDate
     */
    public LocalDate getEventDate() {
        return LocalDate.of(year, month, day);
    }

    /**
     * Get formatted date string
     */
    public String getFormattedDate() {
        return String.format("%s %d, %d", getMonthName(), day, year);
    }

    /**
     * Get month name from month number
     */
    private String getMonthName() {
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        return months[month];
    }

    /**
     * Get short description (first 100 characters)
     */
    public String getShortDescription() {
        if (description == null || description.length() <= 100) {
            return description;
        }
        return description.substring(0, 100) + "...";
    }

    @Override
    public String toString() {
        return String.format("Event{id=%d, date=%s, title='%s', category='%s'}", 
                           id, getFormattedDate(), title, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event event = (Event) obj;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
