package com.example.androidgaya.main.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.example.androidgaya.R;
import com.example.androidgaya.login.ui.LoginActivity;
import com.example.androidgaya.repositories.reminder.RemindersRepository;

public class MainActivity extends AppCompatActivity {
    String username = "";
    Toolbar toolbar;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void logout() {
        prefs.edit().putBoolean(getString(R.string.isLoggedIn), false).apply();
        prefs.edit().putString(getString(R.string.username), "").apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    public void init() {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        prefs = getApplicationContext().getSharedPreferences(getString(R.string.user_details_sp), MODE_PRIVATE);
        username = prefs.getString(getString(R.string.username), "");
        RemindersRepository.getInstance().addUsername(username);
        getSupportActionBar().setTitle(getString(R.string.toolbar_main, username));
    }
}

