package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class RemaindersFragment extends Fragment implements RemainderAdapter.ItemClickListener {

    private RemainderAdapter remainderAdapter;
    DetailsFragment detailsFragment = new DetailsFragment();
    private String chosenRemHeader = "";
    private String chosenRemDescription = "";
    private int chosenYear = 1970;
    private int chosenMonth = 1;
    private int chosenDay = 1;
    private int chosenHour = 0;
    private int chosenMinutes = 0;
    SharedPreferences prefs;
    static FloatingActionButton addFab;

    public RemaindersFragment() {
        // Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View remaindersView = inflater.inflate(R.layout.fragment_remainders, container, false);
        Map<String, ArrayList<Remainder>> remaindersMap = RemaindersBase.get().getRemaindersMap();
        RecyclerView recyclerViewRemainders = remaindersView.findViewById(R.id.recycler_view_remainders);
        addFab = remaindersView.findViewById(R.id.fab);
        prefs = RemaindersFragment.this.getContext()
                .getSharedPreferences(getString(R.string.userdetails), Context.MODE_PRIVATE);
        recyclerViewRemainders.setHasFixedSize(true);

        addFab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                detailsFragment = new DetailsFragment();
                ((MainActivity)getActivity()).changeFragment(detailsFragment);
                ((MainActivity)getActivity()).changeToolbar("Add Remainder", true);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RemaindersFragment.this.getContext());
        recyclerViewRemainders.setLayoutManager(layoutManager);

        String username = prefs.getString(getString(R.string.username), "");
        RemaindersBase.get().addUsername(username);

        remainderAdapter = new RemainderAdapter(RemaindersFragment.this.getContext(), remaindersMap.get(username));
        recyclerViewRemainders.setAdapter(remainderAdapter);
        recyclerViewRemainders.setLayoutManager(new LinearLayoutManager(RemaindersFragment.this.getContext()));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(remainderAdapter, username));
        itemTouchHelper.attachToRecyclerView(recyclerViewRemainders);

        recyclerViewRemainders.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(View view, int position) {
                detailsFragment = new DetailsFragment();
                chosenRemHeader = remainderAdapter.getItem(position).getHeader();
                chosenRemDescription = remainderAdapter.getItem(position).getDescription();
                chosenYear = remainderAdapter.getItem(position).getYear();
                chosenMonth = remainderAdapter.getItem(position).getMonth();
                chosenDay = remainderAdapter.getItem(position).getDayOfMonth();
                chosenHour = remainderAdapter.getItem(position).getHour();
                chosenMinutes = remainderAdapter.getItem(position).getMinutes();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle arguments = new Bundle();
                arguments.putInt("Position", position);
                arguments.putString("Header", chosenRemHeader);
                arguments.putString("Description", chosenRemDescription);
                arguments.putInt("Year", chosenYear);
                arguments.putInt("Month", chosenMonth);
                arguments.putInt("Day", chosenDay);
                arguments.putInt("Hour", chosenHour);
                arguments.putInt("Minutes", chosenMinutes);
                detailsFragment.setArguments(arguments);

                transaction.replace(R.id.fragment_container, detailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                ((AppCompatActivity) getActivity()).
                        getSupportActionBar().setTitle("Edit Remainder");
                ((AppCompatActivity) getActivity()).
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }));

        return remaindersView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            profile();
            return true;
        }
        else if (item.getItemId() == R.id.action_logout){
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        ((MainActivity)getActivity()).logout();
    }

    @SuppressLint("RestrictedApi")
    public void profile() {
        ProfileFragment profileFragment = new ProfileFragment();
        ((MainActivity)getActivity()).changeFragment(profileFragment);
        ((MainActivity)getActivity()).changeToolbar("Profile", true);
    }


    @Override
    public void onItemClick(View view, int position) {}
}

