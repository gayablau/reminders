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

    public void editRemainder(int position, Remainder updatedRemainder, String username) {
        remaindersMap.get(username).remove(position);
        remaindersMap.get(username).add(position, updatedRemainder);
    }

    public void deleteRemainder(int position, String username) {
        remaindersMap.get(username).remove(position);
    }
}
