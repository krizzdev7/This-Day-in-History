package com.thisdayhistory.util;

import java.util.Calendar;

public class DateUtil {

    public static boolean isValidDate(int month, int day, Integer year) {
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        return true;
    }

    public static boolean isLeapYear(Integer year) {
        if (year == null) {
            return false; // Cannot determine without a year
        }
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
