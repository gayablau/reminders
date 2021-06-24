package com.example.androidgaya.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidgaya.R;
import com.example.androidgaya.login.ui.LoginActivity;
import com.example.androidgaya.main.vm.MainViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    String username = "";
    Toolbar toolbar;
    MainViewModel viewModel;

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
        viewModel.setIsLoggedIn(false);
        viewModel.setUsername("");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    public void init() {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        username = viewModel.getUsername();
        assert username != null;
        viewModel.addUsername(username);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.toolbar_main, username));
    }
}

