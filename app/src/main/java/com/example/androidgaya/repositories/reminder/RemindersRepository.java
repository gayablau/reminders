package com.example.androidgaya.repositories.reminder;

import com.example.androidgaya.repositories.Reminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemindersRepository {
    private final Map<String, ArrayList<Reminder>> remindersMap;

    private RemindersRepository() {
        remindersMap = new HashMap<>();
    }

    private static RemindersRepository INSTANCE;

    public static RemindersRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemindersRepository();
        }
        return INSTANCE;
    }

    public Reminder getReminderByID(String username, String id) {
        for(Reminder reminder : remindersMap.get(username)) {
            if(reminder.getId().equals(id)){
                return reminder;
            }
        }
        return null;
    }

    public Map<String, ArrayList<Reminder>> getRemindersMap() {
        return remindersMap;
    }

    public boolean isUsernameExists(String username) {
        return remindersMap.containsKey(username);
    }

    public void addUsername(String username) {
        if (!isUsernameExists(username)) {
            remindersMap.put(username, new ArrayList<>());
        }
    }

    public void editUsername(String oldUsername, String newUsername) {
        if (isUsernameExists(oldUsername)) {
            remindersMap.put(newUsername, remindersMap.get(oldUsername));
            remindersMap.remove(oldUsername);
        }
        else { addUsername(newUsername); }
    }

    public void addReminder(Reminder reminder, String username) {
        remindersMap.get(username).add(reminder);
    }

    public void editReminder(Reminder updatedReminder, String username) {
        for(Reminder reminder : remindersMap.get(username)) {
            if(reminder.getId().contains(updatedReminder.getId())){
                reminder.setHeader(updatedReminder.getHeader());
                reminder.setDescription(updatedReminder.getDescription());
                reminder.setYear(updatedReminder.getYear());
                reminder.setMonth(updatedReminder.getMonth());
                reminder.setDayOfMonth(updatedReminder.getDayOfMonth());
                reminder.setHour(updatedReminder.getHour());
                reminder.setMinutes(updatedReminder.getMinutes());
                reminder.setDay(updatedReminder.getDay());
            }
        }
    }

    public void deleteReminder(int position, String username) {
        remindersMap.get(username).remove(position);
    }

    public void deleteReminderById(String id, String username) {
        for(Reminder reminder : remindersMap.get(username)) {
            if(reminder.getId().contains(id)){
                remindersMap.get(username).remove(reminder);
            }
        }
    }
}
