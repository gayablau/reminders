package com.example.androidgaya;

public class Remainder {

    protected String header, description, hour, date, day;

    public Remainder() {
    }

    public Remainder(String header, String description, String hour, String date, String day) {
        this.header = header;
        this.description = description;
        this.hour = hour;
        this.date = date;
        this.day = day;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
