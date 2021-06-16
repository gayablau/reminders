package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class ProfileFragment extends Fragment {

    private EditText usernameET;
    private static String username;
    SharedPreferences prefs;
    Navigator navigator = new Navigator();

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onBackPressed() {
        RemaindersFragment remaindersFragment = new RemaindersFragment();
        navigator.changeFragment(remaindersFragment, getContext());
    }

    public String getUsernameETValue() {
        return usernameET.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    public void save() {
        if (RemaindersBase.get().isUsernameExists(getUsernameETValue())) {
            Toast.makeText(getActivity(), "user exists", Toast.LENGTH_LONG).show();
        }
        else {
            RemaindersBase.get().editUsername(username, getUsernameETValue());
            username = getUsernameETValue();
            prefs.edit().putString(getString(R.string.username), username).apply();
            RemaindersFragment remaindersFragment = new RemaindersFragment();
            navigator.changeFragment(remaindersFragment, getContext());
        }
    }

    public void init(View view) {
        usernameET = view.findViewById(R.id.profile_username_et);
        prefs = getContext().getSharedPreferences(getString(R.string.user_details_sp), Context.MODE_PRIVATE);
        username = prefs.getString(getString(R.string.username), "");
        usernameET.setText(username);
        navigator.changeToolbar(getString(R.string.profile), true, getContext());
    }
}
