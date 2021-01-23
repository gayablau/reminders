package com.example.androidgaya;

public class Remainder {

    private String header, description, time, date, day;
    private int year, month, dayOfMonth, hour, minutes;

    public Remainder() {
    }

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
        }
        else {
            this.time = hour + ":" + minutes;
        }
    }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }

    public void setMonth(int month) { this.month = month; }

    public int getDayOfMonth() { return dayOfMonth; }

    public void setDayOfMonth(int dayOfMonth) { this.dayOfMonth = dayOfMonth; }

    public String getHeader() { return header; }

    public void setHeader(String header) { this.header = header; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getDay() { return day; }

    public void setDay(String day) { this.day = day; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public int getHour() { return hour; }

    public void setHour(int hour) { this.hour = hour; }

    public int getMinutes() { return minutes; }

    public void setMinutes(int minutes) { this.minutes = minutes; }
}
