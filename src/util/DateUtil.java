package util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * DateUtil Class
 * Utility methods for date formatting and manipulation
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class DateUtil {

    private static final DateTimeFormatter FULL_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMMM d, yyyy");
    
    private static final DateTimeFormatter MONTH_DAY_FORMATTER = 
        DateTimeFormatter.ofPattern("MMMM d");
    
    private static final DateTimeFormatter SHORT_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM d, yyyy");

    /**
     * Private constructor to prevent instantiation
     */
    private DateUtil() {
    }

    /**
     * Format date to full string (e.g., "October 21, 2025")
     * 
     * @param date LocalDate to format
     * @return Formatted string
     */
    public static String formatFullDate(LocalDate date) {
        return date.format(FULL_DATE_FORMATTER);
    }

    /**
     * Format date to month and day only (e.g., "October 21")
     * 
     * @param date LocalDate to format
     * @return Formatted string
     */
    public static String formatMonthDay(LocalDate date) {
        return date.format(MONTH_DAY_FORMATTER);
    }

    /**
     * Format date to short format (e.g., "Oct 21, 2025")
     * 
     * @param date LocalDate to format
     * @return Formatted string
     */
    public static String formatShortDate(LocalDate date) {
        return date.format(SHORT_DATE_FORMATTER);
    }

    /**
     * Get month name from month number
     * 
     * @param month Month number (1-12)
     * @return Month name
     */
    public static String getMonthName(int month) {
        if (month < 1 || month > 12) {
            return "Invalid";
        }
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    /**
     * Get short month name from month number
     * 
     * @param month Month number (1-12)
     * @return Short month name (e.g., "Jan")
     */
    public static String getShortMonthName(int month) {
        if (month < 1 || month > 12) {
            return "Invalid";
        }
        return Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    /**
     * Get today's date
     * 
     * @return LocalDate for today
     */
    public static LocalDate getToday() {
        return LocalDate.now();
    }

    /**
     * Get yesterday's date
     * 
     * @return LocalDate for yesterday
     */
    public static LocalDate getYesterday() {
        return LocalDate.now().minusDays(1);
    }

    /**
     * Get tomorrow's date
     * 
     * @return LocalDate for tomorrow
     */
    public static LocalDate getTomorrow() {
        return LocalDate.now().plusDays(1);
    }

    /**
     * Check if a date is valid
     * 
     * @param year Year
     * @param month Month (1-12)
     * @param day Day of month
     * @return true if valid
     */
    public static boolean isValidDate(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parse date from month and day
     * Uses current year or a default year
     * 
     * @param month Month (1-12)
     * @param day Day of month
     * @return LocalDate
     */
    public static LocalDate parseMonthDay(int month, int day) {
        return LocalDate.of(2000, month, day);
    }

    /**
     * Get ordinal suffix for day (st, nd, rd, th)
     * 
     * @param day Day of month
     * @return Day with suffix (e.g., "1st", "22nd")
     */
    public static String getDayWithSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1: return day + "st";
            case 2: return day + "nd";
            case 3: return day + "rd";
            default: return day + "th";
        }
    }

    /**
     * Format date with ordinal suffix (e.g., "October 21st, 2025")
     * 
     * @param date LocalDate to format
     * @return Formatted string with ordinal
     */
    public static String formatWithOrdinal(LocalDate date) {
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String day = getDayWithSuffix(date.getDayOfMonth());
        return String.format("%s %s, %d", month, day, date.getYear());
    }

    /**
     * Get day of week name
     * 
     * @param date LocalDate
     * @return Day name (e.g., "Monday")
     */
    public static String getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    /**
     * Get number of days in month
     * 
     * @param year Year
     * @param month Month (1-12)
     * @return Number of days
     */
    public static int getDaysInMonth(int year, int month) {
        return LocalDate.of(year, month, 1).lengthOfMonth();
    }

    /**
     * Check if year is leap year
     * 
     * @param year Year to check
     * @return true if leap year
     */
    public static boolean isLeapYear(int year) {
        return LocalDate.of(year, 1, 1).isLeapYear();
    }

    /**
     * Format as ISO date string (yyyy-MM-dd)
     * 
     * @param date LocalDate
     * @return ISO formatted string
     */
    public static String toIsoString(LocalDate date) {
        return date.toString();
    }

    /**
     * Parse ISO date string (yyyy-MM-dd)
     * 
     * @param dateString ISO date string
     * @return LocalDate
     */
    public static LocalDate fromIsoString(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    /**
     * Get friendly date description (e.g., "Today", "Yesterday", "Tomorrow", or full date)
     * 
     * @param date LocalDate
     * @return Friendly description
     */
    public static String getFriendlyDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        
        if (date.equals(today)) {
            return "Today";
        } else if (date.equals(today.minusDays(1))) {
            return "Yesterday";
        } else if (date.equals(today.plusDays(1))) {
            return "Tomorrow";
        } else {
            return formatFullDate(date);
        }
    }

    /**
     * Calculate years ago from a year
     * 
     * @param year Historical year
     * @return Years ago
     */
    public static int yearsAgo(int year) {
        return LocalDate.now().getYear() - year;
    }

    /**
     * Format years ago (e.g., "146 years ago")
     * 
     * @param year Historical year
     * @return Formatted string
     */
    public static String formatYearsAgo(int year) {
        int years = yearsAgo(year);
        if (years == 0) {
            return "This year";
        } else if (years == 1) {
            return "1 year ago";
        } else {
            return years + " years ago";
        }
    }
}
