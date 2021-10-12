package com.example.androidgaya.profile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.example.androidgaya.factory.ViewModelFactory;
import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.repositories.di.AppDataGetter;
import com.example.androidgaya.repositories.socket.SocketRepo;
import com.example.androidgaya.util.MainNavigator;
import com.example.androidgaya.R;
import com.example.androidgaya.profile.viewmodel.ProfileViewModel;

import javax.inject.Inject;

public class ProfileFragment extends Fragment {

    private EditText usernameET;
    private static String username;
    MainNavigator nav;
    ProfileViewModel viewModel;

    @Inject
    ViewModelFactory factory;

    public ProfileFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((AppDataGetter) getActivity().getApplicationContext()).getAppComponent().injectProfile(this);
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
        if (viewModel.isUsernameExists(getNewUsername())) {
            Toast.makeText(getActivity(), getString(R.string.user_exists), Toast.LENGTH_LONG).show();
        } else {
            viewModel.editUsername(getNewUsername());
            username = getNewUsername();
            viewModel.setUsername(username);
            nav.toRemindersFragment();
        }
    }

    public void init(View view) {
        usernameET = view.findViewById(R.id.profile_username_et);
        nav = ((MainActivityInterface) getActivity()).getNavigator();
        ((MainActivityInterface) getActivity()).changeToolbar(getString(R.string.profile), true);
        viewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);
        username = viewModel.getUsername();
        usernameET.setText(username);
    }
}
