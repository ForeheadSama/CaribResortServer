package com.caribresort.client.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Date implements Serializable{
    private static final long serialVersionUID = 1L;

    private int day; // Day of the month
    private int month; // Month of the year
    private int year; // Year
    private int hour; // Hour of the day
    private int minutes; // Minutes
    private int seconds; // Seconds
    private Period period; // AM/PM

    // Enum to represent the AM/PM period
    public static enum Period {AM, PM};

    // ---------- DEFAULT CONSTRUCTOR
    public Date() {
        // Initialize to current date and time
        Calendar calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.seconds = calendar.get(Calendar.SECOND);
        this.period = (hour < 12) ? Period.AM : Period.PM;
    }

    // ---------- PARAMETERIZED CONSTRUCTORS
    public Date(int day, int month, int year, Period period) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.period = period;
    }

    public Date(int day, int month, int year, int hour, int minutes, int seconds, Period period) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
        this.period = period;
    }

    // ---------- GETTERS AND SETTERS
    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getHour() { return hour; }
    public void setHour(int hour) { this.hour = hour; }

    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes; }

    public int getSeconds() { return seconds; }
    public void setSeconds(int seconds) { this.seconds = seconds; }

    public Period getPeriod() { return period; }
    public void setPeriod(Period period) { this.period = period; }

    // Updated method to convert Date object to MySQL DATETIME string
    public static String convertToDateTimeString(Date customDate) {
        int hour24 = customDate.getHour();

        // Convert 12-hour format to 24-hour format
        if (customDate.getPeriod() == Period.PM && hour24 != 12) {
            hour24 += 12;
        } else if (customDate.getPeriod() == Period.AM && hour24 == 12) {
            hour24 = 0;
        }

        // Validate hours (0-23)
        hour24 = Math.min(Math.max(hour24, 0), 23);

        // Validate minutes and seconds (0-59)
        int validMinutes = Math.min(Math.max(customDate.getMinutes(), 0), 59);
        int validSeconds = Math.min(Math.max(customDate.getSeconds(), 0), 59);

        // Format the date as YYYY-MM-DD HH:MM:SS
        return String.format("%04d-%02d-%02d %02d:%02d:%02d",
            customDate.getYear(),
            customDate.getMonth(),
            customDate.getDay(),
            hour24,
            validMinutes,
            validSeconds
        );
    }

    // Updated method to get current date and time in MySQL DATETIME format
    public static String getCurrentDateTimeString() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    // Updated method to convert MySQL DATETIME string to Date object
    public static Date convertFromDatetimeString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);

        int day = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();
        int hour24 = localDateTime.getHour();
        int minutes = localDateTime.getMinute();
        int seconds = localDateTime.getSecond();

        // Convert 24-hour to 12-hour format
        Period period = hour24 >= 12 ? Period.PM : Period.AM;
        int hour12 = hour24 % 12;
        if (hour12 == 0) {
            hour12 = 12;
        }

        return new Date(day, month, year, hour12, minutes, seconds, period);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%04d %02d:%02d:%02d %s", 
            day, month, year, hour, minutes, seconds, period);
    }
}
