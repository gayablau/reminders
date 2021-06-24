package com.example.androidgaya.util;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidgaya.R;
import com.example.androidgaya.main.ui.MainActivity;

public class Navigator {

    public void changeFragment(Fragment fragment, Context context) {
        FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void changeToolbar(String title, Boolean back, Context context) {
        if (((MainActivity)context).getSupportActionBar() != null) {
            ((MainActivity)context).getSupportActionBar().setTitle(title);
            ((MainActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(back);
        }
        ((MainActivity)context).invalidateOptionsMenu();
    }
}
