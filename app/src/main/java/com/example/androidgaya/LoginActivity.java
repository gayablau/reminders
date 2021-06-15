package com.example.androidgaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        init();


        if (savedInstanceState != null) {
            usernameEditText.setText(savedInstanceState.getString(getString(R.string.username), ""));
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { login(); }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginButton.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) { }
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

    public void init() {
        setContentView(R.layout.activity_login);
        prefs = getApplicationContext().getSharedPreferences(getString(R.string.user_details_sp), MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean(getString(R.string.isLoggedIn), false);
        if (isLoggedIn) { goToMainActivity(); }
        getSupportActionBar().hide();
        usernameEditText = findViewById(R.id.username_et);
        loginButton = findViewById(R.id.login_btn);
        imageView = findViewById(R.id.image_clock);
        imageView.setBackgroundResource(R.drawable.alarm_clock_img);
        username = prefs.getString(getString(R.string.username), "");
    }

    public void login() {
        goToMainActivity();
        prefs.edit().putBoolean(getString(R.string.isLoggedIn), true).apply();
        prefs.edit().putString(getString(R.string.username), usernameEditText.getText().toString()).apply();
    }
}
