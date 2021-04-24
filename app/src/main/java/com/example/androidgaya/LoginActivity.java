package com.example.androidgaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    Button loginButton;
    ImageView imageView;
    int orientation;
    boolean isLoggedIn = false;
    String username = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        orientation = this.getResources().getConfiguration().orientation;
        usernameEditText = findViewById(R.id.username);
        loginButton = findViewById(R.id.login);
        imageView = findViewById(R.id.image_clock);
        if (savedInstanceState != null) {
            usernameEditText.setText(savedInstanceState.getString("username", ""));
        }
        imageView.setBackgroundResource(R.drawable.alarm_clock);
        // Get info from shared preferences - is user logged in and username
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        username = prefs.getString("username", "");

        // If user is already logged in, go to main activity
        if (isLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        // Click Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Main Activity and close Login Activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
                // Save in shared preferences username and that the user is logged in
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                prefs.edit().putBoolean("isLoggedIn", true).apply();
                prefs.edit().putString("username", usernameEditText.getText().toString()).apply();
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Set Login button enabled or not according to input
                loginButton.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", usernameEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
