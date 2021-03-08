package com.example.androidgaya;

public class Remainder {

    private final String header, description, time, date, day;
    private final int year, month, dayOfMonth, hour, minutes;

    public Remainder(String header, String description, int hour, int minutes, String day,
                     int year, int month, int dayOfMonth) {
        this.header = header;
        this.description = description;
        this.hour = hour;
        this.minutes = minutes;
        this.date = dayOfMonth + "/" + month + "/" + year;
        this.day = day;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        if (minutes < 10) {
            this.time = hour + ":0" + minutes;
        } else {
            this.time = hour + ":" + minutes;
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }
}
