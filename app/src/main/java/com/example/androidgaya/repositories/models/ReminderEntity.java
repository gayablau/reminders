package com.example.androidgaya.repositories.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "reminders")
public class ReminderEntity {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "header")
    private String header;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "user")
    private String user;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "createdAt")
    private long createdAt;

    public ReminderEntity(int id,
                          String header,
                          String description,
                          String user,
                          long time,
                          long createdAt) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.user = user;
        this.time = time;
        if (createdAt == 0) {
            Calendar helperCalendar = Calendar.getInstance();
            this.createdAt = helperCalendar.getTimeInMillis();
        } else {
            this.createdAt = createdAt;
        }
    }

    public int getYear() {
        Calendar helperCalendar = Calendar.getInstance();
        helperCalendar.setTimeInMillis(time);
        return helperCalendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar helperCalendar = Calendar.getInstance();
        helperCalendar.setTimeInMillis(time);
        return helperCalendar.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        Calendar helperCalendar = Calendar.getInstance();
        helperCalendar.setTimeInMillis(time);
        return helperCalendar.get(Calendar.DATE);
    }

    public int getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    public int getHour() {
        Calendar helperCalendar = Calendar.getInstance();
        helperCalendar.setTimeInMillis(time);
        return helperCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes() {
        Calendar helperCalendar = Calendar.getInstance();
        helperCalendar.setTimeInMillis(time);
        return helperCalendar.get(Calendar.MINUTE);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCalendar() {
        Calendar helperCalendar = Calendar.getInstance();
        helperCalendar.setTimeInMillis(time);
        return helperCalendar;
    }

    public String getUser() {
        return user;
    }

    public void setUsername(String user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}
