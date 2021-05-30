package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
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

    public ProfileFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        username = prefs.getString(getString(R.string.username), "");
        usernameET.setText(username);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        RemaindersFragment remaindersFragment = new RemaindersFragment();
        ((MainActivity) getActivity()).changeFragment(remaindersFragment);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public String getUsernameETValue() {
        return usernameET.getText().toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = ProfileFragment.this.getContext()
                .getSharedPreferences(getString(R.string.userdetails), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        usernameET = profileView.findViewById(R.id.edit_text_username);
        ((MainActivity)getActivity()).changeToolbar(getString(R.string.profile), true);
        return profileView;
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
            ((MainActivity)getActivity()).changeFragment(remaindersFragment);
        }
    }
}
