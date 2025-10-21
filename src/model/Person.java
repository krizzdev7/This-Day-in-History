package model;

import java.time.LocalDate;

/**
 * Person Model Class
 * Represents a famous person with birth and death information
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class Person {
    private int id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    private Integer birthMonth;
    private Integer birthDay;
    private Integer deathMonth;
    private Integer deathDay;
    private String category;
    private String description;
    private String imageUrl;

    /**
     * Default constructor
     */
    public Person() {
    }

    /**
     * Constructor with essential fields
     */
    public Person(String name, Integer birthYear, Integer birthMonth, Integer birthDay,
                  String category, String description) {
        this.name = name;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.category = category;
        this.description = description;
    }

    /**
     * Full constructor
     */
    public Person(int id, String name, Integer birthYear, Integer deathYear,
                  Integer birthMonth, Integer birthDay, Integer deathMonth, Integer deathDay,
                  String category, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.deathMonth = deathMonth;
        this.deathDay = deathDay;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public Integer getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Integer birthMonth) {
        this.birthMonth = birthMonth;
    }

    public Integer getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Integer birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getDeathMonth() {
        return deathMonth;
    }

    public void setDeathMonth(Integer deathMonth) {
        this.deathMonth = deathMonth;
    }

    public Integer getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(Integer deathDay) {
        this.deathDay = deathDay;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Get birth date as LocalDate (year 2000 used for recurring dates)
     */
    public LocalDate getBirthDate() {
        if (birthMonth != null && birthDay != null) {
            int year = birthYear != null ? birthYear : 2000;
            return LocalDate.of(year, birthMonth, birthDay);
        }
        return null;
    }

    /**
     * Get death date as LocalDate (year 2000 used for recurring dates)
     */
    public LocalDate getDeathDate() {
        if (deathMonth != null && deathDay != null) {
            int year = deathYear != null ? deathYear : 2000;
            return LocalDate.of(year, deathMonth, deathDay);
        }
        return null;
    }

    /**
     * Get formatted birth date string
     */
    public String getFormattedBirthDate() {
        if (birthMonth != null && birthDay != null) {
            String date = String.format("%s %d", getMonthName(birthMonth), birthDay);
            if (birthYear != null) {
                date += ", " + birthYear;
            }
            return date;
        }
        return "Unknown";
    }

    /**
     * Get formatted death date string
     */
    public String getFormattedDeathDate() {
        if (deathMonth != null && deathDay != null) {
            String date = String.format("%s %d", getMonthName(deathMonth), deathDay);
            if (deathYear != null) {
                date += ", " + deathYear;
            }
            return date;
        }
        return "N/A";
    }

    /**
     * Get lifespan string (e.g., "1879-1955")
     */
    public String getLifespan() {
        StringBuilder lifespan = new StringBuilder();
        if (birthYear != null) {
            lifespan.append(birthYear);
        } else {
            lifespan.append("?");
        }
        lifespan.append(" - ");
        if (deathYear != null) {
            lifespan.append(deathYear);
        } else {
            lifespan.append("Present");
        }
        return lifespan.toString();
    }

    /**
     * Check if person is still alive
     */
    public boolean isAlive() {
        return deathYear == null;
    }

    /**
     * Get age at death or current age
     */
    public Integer getAge() {
        if (birthYear != null) {
            int endYear = deathYear != null ? deathYear : LocalDate.now().getYear();
            return endYear - birthYear;
        }
        return null;
    }

    /**
     * Get month name from month number
     */
    private String getMonthName(int month) {
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
        return String.format("Person{id=%d, name='%s', lifespan=%s, category='%s'}", 
                           id, name, getLifespan(), category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
