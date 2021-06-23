package com.example.androidgaya.reminders.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Map;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.androidgaya.Navigator;
import com.example.androidgaya.R;
import com.example.androidgaya.details.ui.DetailsFragment;
import com.example.androidgaya.main.ui.MainActivity;
import com.example.androidgaya.reminders.vm.RemindersViewModel;
import com.example.androidgaya.repositories.Reminder;
import com.example.androidgaya.repositories.reminder.RemindersRepo;
import com.example.androidgaya.profile.ui.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RemindersFragment extends Fragment {

    private String id = "";
    String username;
    //SharedPreferences prefs;
    FloatingActionButton addFab;
    RecyclerView recyclerViewReminders;
    Map<String, ArrayList<Reminder>> remindersMap;
    Navigator navigator = new Navigator();
    RemindersViewModel viewModel;

    public RemindersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View remindersView = inflater.inflate(R.layout.fragment_reminders, container, false);
        remindersMap = RemindersRepo.getInstance().getRemindersMap();
        recyclerViewReminders = remindersView.findViewById(R.id.recycler_view_reminders);
        addFab = remindersView.findViewById(R.id.add_fab);
        //prefs = getContext().getSharedPreferences(getString(R.string.user_details_sp), Context.MODE_PRIVATE);
        recyclerViewReminders.setHasFixedSize(true);

        username = viewModel.getUsername();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigator.changeToolbar(getString(R.string.toolbar_main, username), false, getContext());

        addFab.setOnClickListener(view -> {
            DetailsFragment detailsFragment = new DetailsFragment();
            navigator.changeFragment(detailsFragment, getContext());
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RemindersFragment.this.getContext());
        recyclerViewReminders.setLayoutManager(layoutManager);
        return remindersView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RemindersViewModel.class);
        remindersMap = viewModel.getRemindersMap();
        ReminderAdapter reminderAdapter = new ReminderAdapter(remindersMap.get(username), reminder -> {
            DetailsFragment detailsFragment = new DetailsFragment();
            id = reminder.getId();
            Bundle arguments = new Bundle();
            arguments.putString(getString(R.string.id), id);
            detailsFragment.setArguments(arguments);
            navigator.changeFragment(detailsFragment, getContext());
            navigator.changeToolbar(getString(R.string.add_rem), true, getContext());
        });
        recyclerViewReminders.setAdapter(reminderAdapter);
        recyclerViewReminders.setLayoutManager(new LinearLayoutManager(RemindersFragment.this.getContext()));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(reminderAdapter, username));
        itemTouchHelper.attachToRecyclerView(recyclerViewReminders);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            profile();
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        ((MainActivity) getActivity()).logout();
    }

    @SuppressLint("RestrictedApi")
    public void profile() {
        ProfileFragment profileFragment = new ProfileFragment();
        navigator.changeFragment(profileFragment, getContext());
    }

    public void onBackPressed() {
        getActivity().finishAffinity();
    }
}

