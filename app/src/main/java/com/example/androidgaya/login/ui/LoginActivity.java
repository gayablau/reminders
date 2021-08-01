package com.example.androidgaya.login.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidgaya.login.interfaces.LoginActivityInterface;
import com.example.androidgaya.login.viewmodel.LoginViewModel;
import com.example.androidgaya.R;
import com.example.androidgaya.repositories.di.AppComponent;
import com.example.androidgaya.repositories.di.AppModule;
import com.example.androidgaya.repositories.di.DaggerAppComponent;
import com.example.androidgaya.repositories.models.UserEntity;
import com.example.androidgaya.util.LoginNavigator;
import com.example.androidgaya.util.MainNavigator;
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
    private AppComponent appComponent;

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

        //appComponent = DaggerAppComponent.builder().appModule(new AppModule(getApplication())).build();
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
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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

    public void login() {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (viewModel.areDetailesOK(username, password)) {
            viewModel.setUsername(username);
            goToMainActivity();
        }
        else {
            if (viewModel.isUserExists(username)) {
                Toast.makeText(this, getString(R.string.wrong_login), Toast.LENGTH_LONG).show();
            }
            else {
                UserEntity userEntity = new UserEntity(username, password);
                viewModel.createUser(username, password);
                viewModel.addUsername(username);
                viewModel.setUsername(username);
                goToMainActivity();
            }
        }
    }

    @Override
    public LoginNavigator getNavigator() {
        return nav;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
