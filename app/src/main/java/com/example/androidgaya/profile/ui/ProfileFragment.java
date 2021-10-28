package com.example.androidgaya.profile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidgaya.factory.ViewModelFactory;
import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.application.ReminderApplication;
import com.example.androidgaya.repositories.models.LoggedInUserEntity;
import com.example.androidgaya.util.MainNavigator;
import com.example.androidgaya.R;
import com.example.androidgaya.profile.viewmodel.ProfileViewModel;

import java.util.List;

import javax.inject.Inject;

public class ProfileFragment extends Fragment {

    private EditText usernameET;
    private static String username;
    MainNavigator nav;
    ProfileViewModel viewModel;
    LiveData<List<LoggedInUserEntity>> loggedInUserList;

    @Inject
    ViewModelFactory factory;

    public ProfileFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((ReminderApplication) getActivity().getApplicationContext()).getAppComponent().injectProfile(this);
    }

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

    public String getNewUsername() {
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
        viewModel.editUsername(getNewUsername(), (dataFromSocket, dataFromClient) -> {
            if ((Boolean)dataFromSocket[0]) {
                viewModel.updateLoggedIn(dataFromClient.get(0).toString());
                nav.toRemindersFragment();
            } else {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getActivity(),
                                getString(R.string.user_exists),
                                Toast.LENGTH_LONG).show());
            }
            return null;
        });
    }

    public void init(View view) {
        usernameET = view.findViewById(R.id.profile_username_et);
        nav = ((MainActivityInterface) getActivity()).getNavigator();
        ((MainActivityInterface) getActivity()).changeToolbar(getString(R.string.profile), true);
        viewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);

        Observer<List<LoggedInUserEntity>> loggedInObserver = loggedInUserEntities -> {
            if (!loggedInUserEntities.isEmpty()) {
                username = loggedInUserEntities.get(0).getUsername();
                usernameET.setText(username);
            }
        };
        loggedInUserList = viewModel.getLoggedInUser();
        loggedInUserList.observe(getViewLifecycleOwner(), loggedInObserver);

    }
}
