package com.example.androidgaya.reminders.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.androidgaya.login.viewmodel.LoginViewModel;
import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.reminders.recyclerview.ReminderAdapter;
import com.example.androidgaya.reminders.recyclerview.SwipeToDeleteCallback;
import com.example.androidgaya.repositories.models.ReminderEntity;
import com.example.androidgaya.repositories.models.UserEntity;
import com.example.androidgaya.util.MainNavigator;
import com.example.androidgaya.R;
import com.example.androidgaya.main.ui.MainActivity;
import com.example.androidgaya.reminders.viewmodel.RemindersViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RemindersFragment extends Fragment {

    private int id = 0;
    String username;
    FloatingActionButton addFab;
    RecyclerView recyclerViewReminders;
    MutableLiveData<ArrayList<ReminderEntity>> remindersListLive;
    List<ReminderEntity> remindersList;
    MainNavigator nav;
    RemindersViewModel viewModel;
    ReminderAdapter reminderAdapter;

    public RemindersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setAdapter();
        addFab.setOnClickListener(fabView -> {
            add();
        });
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
        nav.toProfileFragment();
    }

    public void onBackPressed() {
        getActivity().finishAffinity();
    }

    public void init(View view) {
        initViewModel();
        recyclerViewReminders = view.findViewById(R.id.recycler_view_reminders);
        addFab = view.findViewById(R.id.add_fab);
        recyclerViewReminders.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RemindersFragment.this.getContext());
        recyclerViewReminders.setLayoutManager(layoutManager);
        username = viewModel.getUsername();
        remindersListLive = viewModel.getRemindersObserver();
        //remindersList
        nav = ((MainActivityInterface) getActivity()).getNavigator();
        ((MainActivityInterface) getActivity()).changeToolbar(getString(R.string.toolbar_main, username), false);
    }

    public void add() {
        nav.toDetailsFragment();
    }

    public void setAdapter() {
        reminderAdapter = new ReminderAdapter(remindersListLive, reminder -> {
            id = reminder.getId();
            nav.toDetailsFragment(id);
        }, reminder -> {

            viewModel.deleteReminder(reminder);
             
            //remindersList.clear();
            //remindersList = viewModel.getRemindersObserver();
            //reminderAdapter.notifyDataSetChanged();
            //viewModel.getMyReminders(username);
            //reminderAdapter.notifyDataSetChanged();

        });


        //reminderAdapter.registerAdapterDataObserver(viewModel.);

        recyclerViewReminders.setAdapter(reminderAdapter);
        recyclerViewReminders.setLayoutManager(new LinearLayoutManager(RemindersFragment.this.getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeToDeleteCallback(reminderAdapter, username)
        );
        itemTouchHelper.attachToRecyclerView(recyclerViewReminders);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RemindersViewModel.class);
        viewModel.getRemindersObserver().observe((LifecycleOwner) RemindersFragment.this.getContext(), (Observer<ArrayList<ReminderEntity>>) reminderEntities -> {
            remindersList = reminderEntities;
            //viewModel.postMyReminders(username);
        });
    }
}