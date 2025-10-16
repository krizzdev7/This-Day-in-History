package com.thisdayhistory.model;

import java.sql.Timestamp;

public class HistoricalFact {
    private int id;
    private int month;
    private int day;
    private Integer year;
    private String event;
    private String category;
    private String source;
    private boolean favorite;
    private Timestamp createdAt;

    public HistoricalFact() {}

    public HistoricalFact(int id, int month, int day, Integer year, String event, String category, String source, boolean favorite, Timestamp createdAt) {
        this.id = id;
        this.month = month;
        this.day = day;
        this.year = year;
        this.event = event;
        this.category = category;
        this.source = source;
        this.favorite = favorite;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        this.event = event;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
