package com.example.androidgaya;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemaindersBase {

    private final Map<String, ArrayList<Remainder>> remaindersMap;

    private RemaindersBase() {
        remaindersMap = new HashMap<String, ArrayList<Remainder>>();
    }

    private static RemaindersBase INSTANCE;

    public static RemaindersBase get() {
        if (INSTANCE == null) {
            INSTANCE = new RemaindersBase();
        }
        return INSTANCE;
    }

    public Map<String, ArrayList<Remainder>> getRemaindersMap() {
        return remaindersMap;
    }

    public boolean isUsernameExists(String username) {
        return remaindersMap.containsKey(username);
    }

    public boolean addUsername(String username) {
        try {
            if (!isUsernameExists(username)) {
                remaindersMap.put(username, new ArrayList<Remainder>());
            }
            else { return false; }
            return true;
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean editUsername(String oldUsername, String newUsername ) {
        try {
            if (isUsernameExists(oldUsername)) {
                remaindersMap.put(newUsername, remaindersMap.get(oldUsername));
                remaindersMap.remove(oldUsername);
            }
            else { addUsername(newUsername); }
            return true;
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean addRemainder(Remainder remainder, String username) {
        try {
            remaindersMap.get(username).add(remainder);
            return true;
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean editRemainder(int position, Remainder updatedRemainder, String username) {
        try {
            remaindersMap.get(username).remove(position);
            remaindersMap.get(username).add(position, updatedRemainder);
            return true;
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean deleteRemainder(int position, String username) {
        try {
            remaindersMap.get(username).remove(position);
            return true;
        }
        catch (Exception ex) {}
        return false;
    }
}
