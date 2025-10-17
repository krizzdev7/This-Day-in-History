package com.thisdayhistory;

import java.util.Scanner;

public class HistoryFacts {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to This Day in History!");
        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();
        System.out.print("Enter day (1-31): ");
        int day = scanner.nextInt();

        String date = String.format("%02d-%02d", month, day);
        String fact = "";

        // Some example facts
        switch (date) {
            case "01-01":
                fact = "In 1801, the Act of Union united Great Britain and Ireland.";
                break;
            case "07-20":
                fact = "In 1969, Neil Armstrong walked on the Moon.";
                break;
            case "08-15":
                fact = "In 1947, India gained independence from British rule.";
                break;
            case "12-25":
                fact = "In 800, Charlemagne was crowned Emperor of the Romans.";
                break;
            default:
                fact = "No fact available for this date yet!";
                break;
        }

        System.out.println("Fact for " + date + ": " + fact);
        scanner.close();
    }
}
