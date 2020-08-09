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
    private static RemaindersBase mRemaindersBase;

    public static RemaindersBase get() {
        if (mRemaindersBase == null) {
            mRemaindersBase = new RemaindersBase();
        }
        return mRemaindersBase;
    }

    public ArrayList<Remainder> getListRemainders() {
        return listRemainders;
    }
}
