package com.example.androidgaya.profile.ui;

import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidgaya.R;
import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.profile.viewmodel.ProfileViewModel;
import com.example.androidgaya.util.MainNavigator;

import java.util.List;

public class ProfileFragment extends Fragment {

    private EditText usernameET;
    private static String username;
    MainNavigator nav;
    ProfileViewModel viewModel;
    LiveData<List<String>> loggedInUserList;

    public ProfileFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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

    public void save() {
        viewModel.editUsername(getNewUsername(), (dataFromSocket, dataFromClient) -> {
            if ((Boolean) dataFromSocket[0]) {
                viewModel.updateLoggedIn(dataFromClient);
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
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        Observer<List<String>> loggedInObserver = loggedInUsernames -> {
            if (!loggedInUsernames.isEmpty()) {
                username = loggedInUsernames.get(0);
                usernameET.setText(username);
            }
        };
        loggedInUserList = viewModel.getLoggedInUser();
        loggedInUserList.observe(getViewLifecycleOwner(), loggedInObserver);
    }
}
