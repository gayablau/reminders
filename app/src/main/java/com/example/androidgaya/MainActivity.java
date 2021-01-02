 package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean isLoggedIn = false;
    String name = "";
    Toolbar toolbar;
    int menuToChoose = R.menu.menu_main;
    RemaindersFragment remaindersFragment = new RemaindersFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    DetailsFragment detailsFragment = new DetailsFragment();
    static FloatingActionButton addFab;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addFab = findViewById(R.id.fab);

        // Click fab add remainder
        addFab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                detailsFragment = new DetailsFragment();

                // Create a new transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_container, detailsFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
                // Set toolbar properties
                getSupportActionBar().setTitle("Add Remainder");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                // Set fab invisible
                addFab.setVisibility(View.GONE);
            }
        });

        // Click back on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                // Check what is the current fragment
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (currentFragment instanceof DetailsFragment) {

                }
                // Managing fragments
                // Create new transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_container, remaindersFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                // Set toolbar properties
                menuToChoose = R.menu.menu_main;
                getSupportActionBar().setTitle("Welcome " + name);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                invalidateOptionsMenu();

                // Set fab visible
                addFab.setVisibility(View.VISIBLE);
            }
        });

        // Get info from shared preferences - is user logged in and username
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        name = prefs.getString("name", "");

        // If user doesn't logged in, go to login activity
        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        // Set toolbar title
        getSupportActionBar().setTitle("Welcome " + name);

        // Managing fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, remaindersFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuToChoose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        switch (item.getItemId()) {
            case R.id.action_profile:
                profile();
                return true;
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        // Update in shared preferences that the user logged out and clear the name
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        prefs.edit().putBoolean("isLoggedIn", false).commit();
        prefs.edit().putString("name", "").commit();
        // Go to Login Activity and close Main Activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        finishAffinity();
        startActivity(intent);
    }

     @SuppressLint("RestrictedApi")
     public void profile() {
         // Create new transaction
         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

         // Replace whatever is in the fragment_container view with this fragment,
         // and add the transaction to the back stack
         transaction.replace(R.id.fragment_container, profileFragment);
         transaction.addToBackStack(null);
         // Commit the transaction
         transaction.commit();
         // Set toolbar properties
         menuToChoose = R.menu.save;
         getSupportActionBar().setTitle("Profile");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         invalidateOptionsMenu();
         // Set fab invisible
         addFab.setVisibility(View.GONE);

     }

     @SuppressLint("RestrictedApi")
     public void save() {
         // Check what is the current fragment
         Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

         if (currentFragment instanceof ProfileFragment) {
             // Save new username in shared preferences
             SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
             name = profileFragment.getUsernameETValue();
             prefs.edit().putString("name", name).commit();

             // Set toolbar properties
             menuToChoose = R.menu.menu_main;
             getSupportActionBar().setTitle("Welcome " + name);
             getSupportActionBar().setDisplayHomeAsUpEnabled(false);
             invalidateOptionsMenu();

             // Managing fragments
             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
             transaction.replace(R.id.fragment_container, remaindersFragment);
             transaction.addToBackStack(null);
             transaction.commit();

             // Set fab visible
             addFab.setVisibility(View.VISIBLE);
         }
         else if (currentFragment instanceof DetailsFragment){
             if (true) {
                 //detailsFragment.isInputValid()
                 // if isEdit == true, edit the remainder. else create a new one
                 if (detailsFragment.getPosition() == -1) {
                     // Add new remainder to singleton
                     RemaindersBase.get().addRemainder(detailsFragment.createRemainderFromInput());

                     // Show toast - remainder added successfully
                     Toast.makeText(this, "remainder added successfully", Toast.LENGTH_LONG).show();
                 }
                 else {
                     RemaindersBase.get().editRemainder(detailsFragment.getPosition(), detailsFragment.createRemainderFromInput());

                     // Show toast - remainder added successfully
                     Toast.makeText(this, "remainder updated successfully", Toast.LENGTH_LONG).show();
                 }

                 // Set toolbar properties
                 menuToChoose = R.menu.menu_main;
                 getSupportActionBar().setTitle("Welcome " + name);
                 getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                 invalidateOptionsMenu();

                 // Clear Input from fragment for the next time
                 //detailsFragment.clearInput();

                 // Managing fragments
                 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                 transaction.replace(R.id.fragment_container, remaindersFragment);
                 transaction.addToBackStack(null);
                 transaction.commit();

                 // Set fab visible
                 addFab.setVisibility(View.VISIBLE);

                 // Finish fragment

             }
             else {
                 // Show toast - please enter a name for your remainder
                 Toast.makeText(this, "please enter a name for your remainder", Toast.LENGTH_SHORT).show();
             }
         }
     }
}