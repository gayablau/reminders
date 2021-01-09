package com.example.androidgaya;

import java.util.ArrayList;
import java.util.List;

public class RemaindersBase {
    private ArrayList<Remainder> listRemainders;
    private RemaindersBase() {
        listRemainders = new ArrayList<Remainder>();
    }
    private static RemaindersBase INSTANCE;

    public static RemaindersBase get() {
        if (INSTANCE == null) {
            INSTANCE = new RemaindersBase();
        }
        return INSTANCE;
    }

    public ArrayList<Remainder> getListRemainders() {
        return listRemainders;
    }

    public boolean addRemainder(Remainder remainder) {
        try {
            listRemainders.add(remainder);
            return true;
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean editRemainder(int position, Remainder updatedRemainder) {
        try {
            listRemainders.remove(position);
            listRemainders.add(position, updatedRemainder);
            return true;
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean deleteRemainder(int position) {
        try {
            listRemainders.remove(position);
            return true;
        }
        catch (Exception ex) {}
        return false;
    }
}
