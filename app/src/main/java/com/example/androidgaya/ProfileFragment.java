package com.example.androidgaya;

import android.content.SharedPreferences;
import android.os.Bundle;
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

    @Override
    public void onResume() {
        super.onResume();
        // Get current username from shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProfileFragment.this.getContext());
        String username = prefs.getString("username", "");
        usernameET.setText(username);
    }

    public String getUsernameETValue() {
        // Returns current input
        return usernameET.getText().toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        usernameET = profileView.findViewById(R.id.edit_text_username);
        return profileView;
    }
}
