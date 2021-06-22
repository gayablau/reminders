package com.example.androidgaya.profile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidgaya.Navigator;
import com.example.androidgaya.R;
import com.example.androidgaya.main.vm.MainViewModel;
import com.example.androidgaya.profile.vm.ProfileViewModel;
import com.example.androidgaya.repositories.reminder.RemindersRepository;
import com.example.androidgaya.reminders.ui.RemindersFragment;


public class ProfileFragment extends Fragment {

    private EditText usernameET;
    private static String username;
    SharedPreferences prefs;
    Navigator navigator = new Navigator();
    ProfileViewModel viewModel;

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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        if (RemindersRepository.getInstance().isUsernameExists(getUsernameETValue())) {
            Toast.makeText(getActivity(), getString(R.string.user_exists), Toast.LENGTH_LONG).show();
        }
        else {
            viewModel.editUsername(username, getUsernameETValue());
            username = getUsernameETValue();
            prefs.edit().putString(getString(R.string.username), username).apply();
            RemindersFragment remindersFragment = new RemindersFragment();
            navigator.changeFragment(remindersFragment, getContext());
        }
    }

    public void init(View view) {
        usernameET = view.findViewById(R.id.profile_username_et);
        prefs = getContext().getSharedPreferences(getString(R.string.user_details_sp), Context.MODE_PRIVATE);
        username = prefs.getString(getString(R.string.username), "");
        usernameET.setText(username);
        navigator.changeToolbar(getString(R.string.profile), true, getContext());
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }
}
