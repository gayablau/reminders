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
    boolean isLoggedIn = false;
    String username = "";
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        usernameEditText = findViewById(R.id.username);
        loginButton = findViewById(R.id.login);
        imageView = findViewById(R.id.image_clock);
        prefs = getApplicationContext().getSharedPreferences(getString(R.string.userdetails), MODE_PRIVATE);
        if (savedInstanceState != null) {
            usernameEditText.setText(savedInstanceState.getString(getString(R.string.username), ""));
        }
        imageView.setBackgroundResource(R.drawable.alarm_clock);
        isLoggedIn = prefs.getBoolean(getString(R.string.isLoggedIn), false);
        username = prefs.getString(getString(R.string.username), "");

        if (isLoggedIn) {
            goToMainActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
                prefs.edit().putBoolean(getString(R.string.isLoggedIn), true).apply();
                prefs.edit().putString(getString(R.string.username), usernameEditText.getText().toString()).apply();
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
        outState.putString(getString(R.string.username), usernameEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        finishAffinity();
        startActivity(intent);
    }
}
