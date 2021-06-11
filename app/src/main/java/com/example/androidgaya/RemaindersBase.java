package com.example.androidgaya;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemaindersBase {

    private final Map<String, ArrayList<Remainder>> remaindersMap;

    private RemaindersBase() {
        remaindersMap = new HashMap<>();
    }

    private static RemaindersBase INSTANCE;

    public static RemaindersBase get() {
        if (INSTANCE == null) {
            INSTANCE = new RemaindersBase();
        }
        return INSTANCE;
    }

    public Remainder getRemainderByID(String username, String id) {
        for(Remainder remainder : remaindersMap.get(username)) {
            if(remainder.getId().equals(id)){
                return remainder;
            }
        }
        return null;
    }

    public Map<String, ArrayList<Remainder>> getRemaindersMap() {
        return remaindersMap;
    }

    public boolean isUsernameExists(String username) {
        return remaindersMap.containsKey(username);
    }

    public void addUsername(String username) {
        if (!isUsernameExists(username)) {
            remaindersMap.put(username, new ArrayList<Remainder>());
        }
    }

    public void editUsername(String oldUsername, String newUsername ) {
        if (isUsernameExists(oldUsername)) {
            remaindersMap.put(newUsername, remaindersMap.get(oldUsername));
            remaindersMap.remove(oldUsername);
        }
        else { addUsername(newUsername); }
    }

    public void addRemainder(Remainder remainder, String username) {
        remaindersMap.get(username).add(remainder);
    }

    public void editRemainder(Remainder updatedRemainder, String username) {
        for(Remainder remainder : remaindersMap.get(username)) {
            if(remainder.getId().contains(updatedRemainder.getId())){
                remainder.setHeader(updatedRemainder.getHeader());
                remainder.setDescription(updatedRemainder.getDescription());
                remainder.setYear(updatedRemainder.getYear());
                remainder.setMonth(updatedRemainder.getMonth());
                remainder.setDayOfMonth(updatedRemainder.getDayOfMonth());
                remainder.setHour(updatedRemainder.getHour());
                remainder.setMinutes(updatedRemainder.getMinutes());
                remainder.setTime(updatedRemainder.getTime());
                remainder.setDate(updatedRemainder.getDate());
                remainder.setDay(updatedRemainder.getDay());
            }
        }
    }

    public void deleteRemainder(int position, String username) {
        remaindersMap.get(username).remove(position);

 /*       for(Remainder remainder : remaindersMap.get(username)) {
            if(remainder.getId().contains(id)){
                remaindersMap.get(username).remove(remainder);
            }
        }*/
    }

    public void deleteRemainderById(String id, String username) {
        //remaindersMap.get(username).remove(position);

        for(Remainder remainder : remaindersMap.get(username)) {
            if(remainder.getId().contains(id)){
                remaindersMap.get(username).remove(remainder);
            }
        }
    }
}
