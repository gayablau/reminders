package com.example.androidgaya.util;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidgaya.R;
import com.example.androidgaya.details.ui.DetailsFragment;
import com.example.androidgaya.login.ui.LoginActivity;
import com.example.androidgaya.main.ui.MainActivity;
import com.example.androidgaya.profile.ui.ProfileFragment;
import com.example.androidgaya.reminders.ui.RemindersFragment;

public class MainNavigator {
    int containerId;
    FragmentActivity activity;

    public MainNavigator(int containerId, FragmentActivity activity) {
        this.containerId = containerId;
        this.activity = activity;
    }

    public void toDetailsFragment(String id) {
        DetailsFragment fragment = DetailsFragment.getInstance(id);
        changeFragment(fragment);
    }

    public void toDetailsFragment() {
        DetailsFragment fragment = DetailsFragment.getInstance();
        changeFragment(fragment);
    }

    public void toProfileFragment() {
        ProfileFragment fragment = new ProfileFragment();
        changeFragment(fragment);
    }

    public void toRemindersFragment() {
        RemindersFragment fragment = new RemindersFragment();
        changeFragment(fragment);
    }

    public void toMainActivity() {
        changeActivity(MainActivity.class);
    }

    public void toLoginActivity() {
        changeActivity(LoginActivity.class);
    }

    public void changeActivity(Class toActivity) {
        Intent intent = new Intent(activity, toActivity);
        activity.finishAffinity();
        activity.startActivity(intent);
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
