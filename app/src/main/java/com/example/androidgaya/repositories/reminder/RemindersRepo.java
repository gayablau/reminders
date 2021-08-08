/*
package com.example.androidgaya.repositories.reminder;

import com.example.androidgaya.repositories.interfaces.ReminderInterface;
import com.example.androidgaya.repositories.models.ReminderEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemindersRepo implements ReminderInterface {

    public void deleteReminder(ReminderEntity reminderEntity, String username) {

        }
  */
/*  private final Map<String, ArrayList<ReminderEntity>> remindersMap;

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

*//*
*/
/*    public ReminderEntity getReminderByID(int id, String username) {
        for(ReminderEntity reminderEntity : remindersMap.get(username)) {
            if(reminderEntity.getId() == id){
                return reminderEntity;
            }
        }
        return null;
    }*//*
*/
/*

 *//*
*/
/*   public Map<String, ArrayList<ReminderEntity>> getRemindersMap() {
        return remindersMap;
    }*//*
*/
/*

 *//*
*/
/*   public ArrayList<ReminderEntity> getRemindersByUsername(String username) {
        return remindersMap.get(username);
    }*//*
*/
/*

  *//*
*/
/*  public boolean isUsernameExists(String username) {
        return remindersMap.containsKey(username);
    }*//*
*/
/*

*//*
*/
/*    public void addUsername(String username) {
        if (!isUsernameExists(username)) {
            remindersMap.put(username, new ArrayList<>());
        }
    }*//*
*/
/*

*//*
*/
/*    public void editUsername(String oldUsername, String newUsername) {
        if (isUsernameExists(oldUsername)) {
            remindersMap.put(newUsername, remindersMap.get(oldUsername));
            remindersMap.remove(oldUsername);
        }
        else { addUsername(newUsername); }
    }*//*
*/
/*

*//*
*/
/*    public void addReminder(ReminderEntity reminderEntity, String username) {
        remindersMap.get(username).add(reminderEntity);
    }*//*
*/
/*

*//*
*/
/*    public void editReminder(ReminderEntity updatedReminderEntity, String username) {
        for(ReminderEntity reminderEntity : remindersMap.get(username)) {
            if(reminderEntity.getId() == updatedReminderEntity.getId()){
                reminderEntity.setHeader(updatedReminderEntity.getHeader());
                reminderEntity.setDescription(updatedReminderEntity.getDescription());
                reminderEntity.setCalendar(updatedReminderEntity.getCalendar());
            }
        }
    }*//*
*/
/*

*//*
*/
/*    public void deleteReminderById(int id, String username) {
        for(ReminderEntity reminderEntity : remindersMap.get(username)) {
            if(reminderEntity.getId() == id){
                remindersMap.get(username).remove(reminderEntity);
            }
        }
    }*//*

}
*/
