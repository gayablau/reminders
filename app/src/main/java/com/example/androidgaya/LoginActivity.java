package com.example.androidgaya;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final ImageView imageView = findViewById(R.id.imageClock);

        // Click Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgressBar.setVisibility(View.VISIBLE);
                // Go to Main Activity and close Login Activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
                // Save in shared preferences username and that the user is logged in
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                prefs.edit().putBoolean("isLoggedIn", true).commit();
                prefs.edit().putString("username", usernameEditText.getText().toString()).commit();
                loadingProgressBar.setVisibility(View.INVISIBLE);
                //אם השם שהוזן קיים, להתחבר ולהציג את ההתראות שלו. אם לא קיים ליצור משתמש עם אותו השם

            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Set Login button enabled or not by input
                if(s.toString().trim().length()==0){
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
