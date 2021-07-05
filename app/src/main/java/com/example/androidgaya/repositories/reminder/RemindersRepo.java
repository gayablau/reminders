package com.example.androidgaya.repositories.reminder;

import com.example.androidgaya.repositories.interfaces.ReminderInterface;
import com.example.androidgaya.repositories.models.Reminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemindersRepo implements ReminderInterface {
    private final Map<String, ArrayList<Reminder>> remindersMap;

    private RemindersRepo() {
        remindersMap = new HashMap<>();
    }

    private static RemindersRepo INSTANCE;

    public static RemindersRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemindersRepo();
        }
        return INSTANCE;
    }

    public Reminder getReminderByID(String id, String username) {
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

    public ArrayList<Reminder> getRemindersByUsername(String username) {
        return remindersMap.get(username);
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
                reminder.setCalendar(updatedReminder.getCalendar());
            }
        }
    }

    public void deleteReminderById(String id, String username) {
        for(Reminder reminder : remindersMap.get(username)) {
            if(reminder.getId().contains(id)){
                remindersMap.get(username).remove(reminder);
            }
        }
    }
}
