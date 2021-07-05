package com.example.androidgaya.repositories.models;

import java.util.Calendar;

public class Reminder {

    private String id;
    private String header;
    private String description;
    private Calendar calendar;

    public Reminder(String id,
                    String header,
                    String description,
                    Calendar calendar) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.calendar = calendar;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        return calendar.get(Calendar.DATE);
    }

    public String getId() { return id; }

    public String getHeader() { return header; }

    public String getDescription() {
        return description;
    }

    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes() {
        return calendar.get(Calendar.MINUTE);
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

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
