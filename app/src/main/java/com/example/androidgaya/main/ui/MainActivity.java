package com.example.androidgaya.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidgaya.R;
import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.main.socket.SocketService;
import com.example.androidgaya.main.viewmodel.MainViewModel;
import com.example.androidgaya.reminders.ui.RemindersFragment;
import com.example.androidgaya.repositories.di.AppComponent;
import com.example.androidgaya.repositories.di.AppModule;
import com.example.androidgaya.repositories.models.LoggedInUserEntity;
import com.example.androidgaya.repositories.socket.SocketHandler;
import com.example.androidgaya.util.MainNavigator;
import com.example.androidgaya.util.NotificationUtils;

import java.util.List;
import java.util.Objects;

import io.socket.client.Socket;


public class MainActivity extends AppCompatActivity implements MainActivityInterface {
    String username;
    int userId;
    LiveData<List<LoggedInUserEntity>> loggedInUserList;
    Toolbar toolbar;
    MainViewModel viewModel;
    MainNavigator nav;
    Socket socket;

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
        viewModel.logout();
        stopService(new Intent(this, SocketService.class));
        nav.toLoginActivity();
        new NotificationUtils().cancelAll(this, viewModel.getMyRemindersIds(userId));
    }

    public void init() {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        initViewModel();
        username = viewModel.getUsernameStr();
        userId = viewModel.getUserId(username);
        nav = new MainNavigator(R.id.fragment_container, this);
        changeToolbar(getString(R.string.toolbar_main, username), false);
        new NotificationUtils().createAll(this, viewModel.getRemindersByUserIdList(userId));
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

    public Socket getSocket() {return socket;}

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Observer<List<LoggedInUserEntity>> loggedInObserver = loggedInUserEntities -> {
            if (!loggedInUserEntities.isEmpty()) {
                username = loggedInUserEntities.get(0).getUsername();
            }
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof RemindersFragment) {
                changeToolbar(getString(R.string.toolbar_main, username), false);
            }
        };

        loggedInUserList = viewModel.loggedInUserList;
        loggedInUserList.observe(this, loggedInObserver);
    }
}

