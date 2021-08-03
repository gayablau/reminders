package com.example.androidgaya.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidgaya.R;
import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.main.viewmodel.MainViewModel;
import com.example.androidgaya.repositories.di.AppComponent;
import com.example.androidgaya.repositories.di.AppModule;
import com.example.androidgaya.util.MainNavigator;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements MainActivityInterface {
    String username = "";
    Toolbar toolbar;
    MainViewModel viewModel;
    MainNavigator nav;

    public static Intent getIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

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
        viewModel.setUsername("");
        nav.toLoginActivity();
    }

    public void init() {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        username = viewModel.getUsername();
        //viewModel.addUsername(username);
        nav = new MainNavigator(R.id.fragment_container, this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.toolbar_main, username));
    }


    @Override
    public MainNavigator getNavigator() {
        return nav;
    }

    @Override
    public void changeToolbar(String title, Boolean back) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(back);
        }
        invalidateOptionsMenu();
    }

    
}

