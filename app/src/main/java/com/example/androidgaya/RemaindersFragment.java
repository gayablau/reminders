package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RemaindersFragment extends Fragment {

    private RemainderAdapter remainderAdapter;
    DetailsFragment detailsFragment = new DetailsFragment();
    private String id = "";
    String username;
    SharedPreferences prefs;
    FloatingActionButton addFab;
    RecyclerView recyclerViewRemainders;
    Map<String, ArrayList<Remainder>> remaindersMap;
    Navigator navigator = new Navigator();

    public RemaindersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View remaindersView = inflater.inflate(R.layout.fragment_remainders, container, false);
        remaindersMap = RemaindersBase.get().getRemaindersMap();
        recyclerViewRemainders = remaindersView.findViewById(R.id.recycler_view_remainders);
        addFab = remaindersView.findViewById(R.id.add_fab);
        prefs = getContext().getSharedPreferences(getString(R.string.user_details_sp), Context.MODE_PRIVATE);
        recyclerViewRemainders.setHasFixedSize(true);

        username = prefs.getString(getString(R.string.username), "");
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigator.changeToolbar(getString(R.string.toolbar_main, username), false, getContext());

        addFab.setOnClickListener(view -> {
            detailsFragment = new DetailsFragment();
            navigator.changeFragment(detailsFragment, getContext());
            navigator.changeToolbar(getString(R.string.add_rem), true, getContext());
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RemaindersFragment.this.getContext());
        recyclerViewRemainders.setLayoutManager(layoutManager);
        return remaindersView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        remaindersMap = RemaindersBase.get().getRemaindersMap();
        remainderAdapter = new RemainderAdapter(remaindersMap.get(username), remainder -> {
            detailsFragment = new DetailsFragment();
            id = remainder.getId();
            Bundle arguments = new Bundle();
            arguments.putString(getString(R.string.id), id);
            detailsFragment.setArguments(arguments);

            navigator.changeFragment(detailsFragment, getContext());
            ((AppCompatActivity) getActivity()).
                    getSupportActionBar().setTitle(R.string.edit_rem);
            ((AppCompatActivity) getActivity()).
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        });
        recyclerViewRemainders.setAdapter(remainderAdapter);
        recyclerViewRemainders.setLayoutManager(new LinearLayoutManager(RemaindersFragment.this.getContext()));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(remainderAdapter, username));
        itemTouchHelper.attachToRecyclerView(recyclerViewRemainders);
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

