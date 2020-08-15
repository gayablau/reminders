package com.example.androidgaya;

import java.util.ArrayList;
import java.util.List;

public class RemaindersBase {
    private ArrayList<Remainder> listRemainders;
    private RemaindersBase() {
        listRemainders = new ArrayList<Remainder>();
        for (int i = 0; i < 100; i++) {
            listRemainders.add(new Remainder("test"+i, "test"+i, "test"+i,"test"+i,"test"+i));
        }
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
}
