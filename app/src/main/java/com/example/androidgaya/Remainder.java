package com.example.androidgaya;

public class Remainder {

    private String id, header, description, day;
    private int year, month, dayOfMonth, hour, minutes;

    public Remainder(String id, String header, String description, int hour, int minutes, String day,
                     int year, int month, int dayOfMonth) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.hour = hour;
        this.minutes = minutes;
        this.day = day;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
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

    public String getId() { return id; }

    public String getHeader() { return header; }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return dayOfMonth + "/" + month + "/" + year;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        if (minutes < 10) { return hour + ":0" + minutes; }
            return hour + ":" + minutes;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
