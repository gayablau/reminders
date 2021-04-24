 package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

 public class MainActivity extends AppCompatActivity {
    String username = "";
    Toolbar toolbar;
    int menuToChoose = R.menu.menu_main;
    RemaindersFragment remaindersFragment = new RemaindersFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    DetailsFragment detailsFragment = new DetailsFragment();
    static FloatingActionButton addFab;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addFab = findViewById(R.id.fab);

        // Click fab add remainder
        addFab.setOnClickListener(view -> {
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
            Objects.requireNonNull(getSupportActionBar()).setTitle("Add Remainder");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Set fab invisible
            addFab.setVisibility(View.GONE);
        });

        // Click back on toolbar
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Get username from shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString("username", "");

        // Set toolbar title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Hello " + username);

        // Managing fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, remaindersFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        // Check what is the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof DetailsFragment || currentFragment instanceof ProfileFragment) {
            // Managing fragments
            // Create new transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.fragment_container, remaindersFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
            // Set toolbar properties
            menuToChoose = R.menu.menu_main;
            getSupportActionBar().setTitle("Hello " + username);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            invalidateOptionsMenu();
            // Set fab visible
            addFab.setVisibility(View.VISIBLE);
        }
        else {
            this.finishAffinity();
        }
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
        Boolean reBoolean = true;
        switch (item.getItemId()) {
            case R.id.action_profile:
                profile();
                break;
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_save:
                save();
                break;
            default:
                reBoolean = super.onOptionsItemSelected(item);
                break;
        }
        return reBoolean;
    }

    public void logout() {
        // Update in shared preferences that the user logged out and clear the name
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        prefs.edit().putBoolean("isLoggedIn", false).apply();
        prefs.edit().putString("username", "").apply();
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
         Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
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
             if (RemaindersBase.get().isUsernameExists(profileFragment.getUsernameETValue())) {
                 Toast.makeText(this, "user exists", Toast.LENGTH_LONG).show();
             }
             else {
                 // Edit name in singleton
                 RemaindersBase.get().editUsername(username, profileFragment.getUsernameETValue());
                 // Save new username in shared preferences
                 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                 username = profileFragment.getUsernameETValue();
                 prefs.edit().putString("username", username).apply();

                 // Set toolbar properties
                 menuToChoose = R.menu.menu_main;
                 Objects.requireNonNull(getSupportActionBar()).setTitle("Hello " + username);
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
         }
         else if (currentFragment instanceof DetailsFragment){
             if (detailsFragment.isInputValid()) {
                 if (detailsFragment.isTimeValid()) {
                     // if position, edit the remainder. else create a new one
                     if (detailsFragment.getPosition() == -1) {
                         // Add new remainder to singleton
                         if (RemaindersBase.get().addRemainder(detailsFragment.createRemainderFromInput(),
                                 username)) {
                             Toast.makeText(this, "remainder added",
                                     Toast.LENGTH_SHORT).show();
                         }
                         else {
                             Toast.makeText(this, "Error adding remainder",
                                     Toast.LENGTH_SHORT).show();
                         }
                     }
                     else {
                         // Edit the chosen remainder and save to singleton
                         if (RemaindersBase.get().editRemainder(detailsFragment.getPosition(),
                                 detailsFragment.createRemainderFromInput(), username)) {
                             Toast.makeText(this, "remainder updated",
                                     Toast.LENGTH_SHORT).show();
                         }
                         else {
                             Toast.makeText(this, "Error updating remainder",
                                     Toast.LENGTH_SHORT).show();
                         }
                     }

                     // Set toolbar properties
                     menuToChoose = R.menu.menu_main;
                     getSupportActionBar().setTitle("Hello " + username);
                     getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                     invalidateOptionsMenu();

                     // Managing fragments
                     FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                     transaction.replace(R.id.fragment_container, remaindersFragment);
                     transaction.addToBackStack(null);
                     transaction.commit();

                     // Set fab visible
                     addFab.setVisibility(View.VISIBLE);
                 } else {
                     Toast.makeText(this, "please select a valid time for your remainder", Toast.LENGTH_SHORT).show();
                 }
             } else {
                 // Show toast - please enter a name for your remainder
                 Toast.makeText(this, "please enter a name for your remainder", Toast.LENGTH_SHORT).show();
             }
         }
     }
}