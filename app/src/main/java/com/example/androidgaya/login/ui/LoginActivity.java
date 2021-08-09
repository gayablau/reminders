package com.example.androidgaya.login.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidgaya.login.interfaces.LoginActivityInterface;
import com.example.androidgaya.login.viewmodel.LoginViewModel;
import com.example.androidgaya.R;
import com.example.androidgaya.repositories.models.UserEntity;
import com.example.androidgaya.util.LoginNavigator;

import java.util.List;
//import dagger.android.DaggerApplication;


public class LoginActivity extends AppCompatActivity implements LoginActivityInterface {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ImageView imageView;
    String username = "";
    String password = "";
    LoginViewModel viewModel;
    LoginNavigator nav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (savedInstanceState != null) {
            usernameEditText.setText(savedInstanceState.getString(getString(R.string.username), ""));
            passwordEditText.setText(savedInstanceState.getString(getString(R.string.password), ""));
        }

        loginButton.setOnClickListener(v -> login());

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

    public static Intent getIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.username), usernameEditText.getText().toString());
        outState.putString(getString(R.string.password), passwordEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void goToMainActivity() {
        nav.toMainActivity();
    }

    public void init() {
        setContentView(R.layout.activity_login);

        initViewModel();
        username = viewModel.getUsername();
        nav = new LoginNavigator(this);
        if (viewModel.isUserLoggedIn()) { nav.toMainActivity(); }
        getSupportActionBar().hide();
        usernameEditText = findViewById(R.id.username_et);
        passwordEditText = findViewById(R.id.password_et);
        loginButton = findViewById(R.id.login_btn);
        imageView = findViewById(R.id.image_clock);
        imageView.setBackgroundResource(R.drawable.alarm_clock_img);

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getUsersObserver().observe(this, new Observer<List<UserEntity>>(){
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                if (!userEntities.isEmpty()) {
                    for (UserEntity user : userEntities) {
                        //TODO something idk
                    }
                }

            }
        });
    }

    public void login() {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (viewModel.areDetailsOK(username, password)) {
            viewModel.setUsername(username);
            goToMainActivity();
        }
        else {
            if (viewModel.isUserExists(username)) {
                Toast.makeText(this, getString(R.string.wrong_login), Toast.LENGTH_LONG).show();
            }
            else {
                viewModel.createUser(username, password);
                Log.i("login","user created with details: " + username + ", " + password);
                viewModel.setUsername(username);
                goToMainActivity();
            }
        }
    }

    @Override
    public LoginNavigator getNavigator() {
        return nav;
    }
}