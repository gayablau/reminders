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

import com.example.androidgaya.login.viewmodel.LoginViewModel;
import com.example.androidgaya.R;
import com.example.androidgaya.login.viewmodel.ViewModelFactory;
import com.example.androidgaya.main.socket.SocketService;
import com.example.androidgaya.util.LoginNavigator;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ImageView imageView;
    String username = "";
    String password = "";
    LoginViewModel viewModel;
    LoginNavigator nav;

    @Inject
    ViewModelFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (savedInstanceState != null) {
            usernameEditText.setText(savedInstanceState.getString(getString(R.string.username_uppercase), ""));
            passwordEditText.setText(savedInstanceState.getString(getString(R.string.password_uppercase), ""));
        }

        loginButton.setOnClickListener(v -> login());

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


    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.username_uppercase), usernameEditText.getText().toString());
        outState.putString(getString(R.string.password_uppercase), passwordEditText.getText().toString());
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
        startService(new Intent(this, SocketService.class));
        initViewModel();
        nav = new LoginNavigator(this);

        if (viewModel.isUserLoggedIn() &&
                viewModel.isUserExists(viewModel.getUsername())) {
            nav.toMainActivity();
            viewModel.connectUser(viewModel.getUsername());
        }

        getSupportActionBar().hide();
        usernameEditText = findViewById(R.id.username_et);
        passwordEditText = findViewById(R.id.password_et);
        loginButton = findViewById(R.id.login_btn);
        imageView = findViewById(R.id.image_clock);
        imageView.setBackgroundResource(R.drawable.alarm_clock_img);

    }

    private void initViewModel() {
        //viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel
    }

    public void login() {
        viewModel.getAllUsers();
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (viewModel.areDetailsOK(username, password)) {
            viewModel.connectUser(username);
            goToMainActivity();
        } else {
            if (viewModel.isUserExists(username)) {
                Toast.makeText(this, getString(R.string.wrong_login), Toast.LENGTH_LONG).show();
            } else {
                viewModel.createNewUser(username, password);
                goToMainActivity();
            }
        }
    }
}
