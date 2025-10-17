package com.thisdayhistory.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Logger;

public class DateUtil {
    private static final Logger LOGGER = Logger.getLogger(DateUtil.class.getName());
    private static final DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMMM d", Locale.ENGLISH);
    private static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);

    private DateUtil() {
        // Private constructor to prevent instantiation
    }

    public static String formatMonthDay(int month, int day) {
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), month, day);
        return date.format(MONTH_DAY_FORMATTER);
    }

    public static String formatFullDate(int month, int day, Integer year) {
        if (year == null) {
            return formatMonthDay(month, day);
        }
        LocalDate date = LocalDate.of(year, month, day);
        return date.format(FULL_DATE_FORMATTER);
    }

    public static boolean isValidDate(int month, int day, Integer year) {
        if (month < 1 || month > 12) {
            return false;
        }

        int daysInMonth;
        if (year != null) {
            try {
                LocalDate date = LocalDate.of(year, month, 1);
                daysInMonth = date.lengthOfMonth();
            } catch (Exception e) {
                LOGGER.warning("Invalid year value: " + year);
                return false;
            }
        } else {
            daysInMonth = Month.of(month).length(LocalDate.now().isLeapYear());
        }

        return day >= 1 && day <= daysInMonth;
    }

    public static boolean isLeapYear(Integer year) {
        if (year == null) {
            return false;
        }
        try {
            return Year.isLeap(year);
        } catch (Exception e) {
            LOGGER.warning("Invalid year for leap year check: " + year);
            return false;
        }
    }

    public static int getDaysInMonth(int month, Integer year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        if (year != null) {
            try {
                return LocalDate.of(year, month, 1).lengthOfMonth();
            } catch (Exception e) {
                LOGGER.warning("Invalid year value: " + year);
                throw new IllegalArgumentException("Invalid year: " + year);
            }
        }
        return Month.of(month).length(LocalDate.now().isLeapYear());
    }

        public static int getMaxDayForMonth(int month) {
            return getDaysInMonth(month, LocalDate.now().getYear());
        }
}
