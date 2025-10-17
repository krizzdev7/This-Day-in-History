package com.thisdayhistory.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class HistoricalFact {
    private Integer id;
    private int month;
    private int day;
    private Integer year;
    private String event;
    private String category;
    private String source;
    private boolean favorite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public HistoricalFact() {}

    public HistoricalFact(int month, int day, Integer year, String event, String category, String source) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.event = event;
        this.category = category;
        this.source = source;
    }
    
        public HistoricalFact(Integer id, int month, int day, int year, String event, String category, boolean favorite, String source) {
            this.id = id;
            this.month = month;
            this.day = day;
            this.year = year;
            this.event = event;
            this.category = category;
            this.favorite = favorite;
            this.source = source;
        }

    // Getters and Setters with validation
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }
        // Note: Actual validation for day based on month should be in service layer
        this.day = day;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        if (event == null || event.trim().isEmpty()) {
            throw new IllegalArgumentException("Event cannot be empty");
        }
        this.event = event.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be empty");
        }
        this.source = source.trim();
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Formatted getters for display
    public String getFormattedDate() {
        String yearStr = year != null ? " " + year : "";
        return String.format("%d-%02d%s", month, day, yearStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricalFact fact = (HistoricalFact) o;
        return month == fact.month &&
               day == fact.day &&
               Objects.equals(year, fact.year) &&
               Objects.equals(event, fact.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, day, year, event);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", getFormattedDate(), event, category);
    }
}
