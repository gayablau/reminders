package com.example.androidgaya;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class ProfileFragment extends Fragment {

    private EditText usernameET;

    public ProfileFragment() {
        // Empty public constructor
    }

    public String getUsernameETValue() {
        // Returns current input
        return usernameET.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameET = view.findViewById(R.id.edit_text_username);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProfileFragment.this.getContext());
        String username = prefs.getString("username", "");
        usernameET.setText(username);
    }
}
