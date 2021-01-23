package com.example.androidgaya;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Map;

public class RemaindersFragment extends Fragment implements RemainderAdapter.ItemClickListener {

    private RecyclerView recyclerViewRemainders;
    private RemainderAdapter remainderAdapter;
    private RecyclerView.LayoutManager layoutManager;
    DetailsFragment detailsFragment = new DetailsFragment();
    private String chosenRemHeader = "";
    private String chosenRemDescription = "";
    private int chosenYear = 1970;
    private int chosenMonth = 1;
    private int chosenDay = 1;
    private int chosenHour = 00;
    private int chosenMinutes = 00;
    private String username = "";

    public RemaindersFragment() {
        // Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View remaindersView = inflater.inflate(R.layout.fragment_remainders, container, false);

        Map<String, ArrayList<Remainder>> remaindersMap = RemaindersBase.get().getRemaindersMap();

        recyclerViewRemainders = (RecyclerView) remaindersView.findViewById(R.id.recyclerViewRemainders);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerViewRemainders.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(RemaindersFragment.this.getContext());
        recyclerViewRemainders.setLayoutManager(layoutManager);

        // Get info from shared preferences - is user logged in and username
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RemaindersFragment.this.getContext());
        username = prefs.getString("username", "");
        RemaindersBase.get().addUsername(username);

        // specify an adapter to convert the array to views
        remainderAdapter = new RemainderAdapter(RemaindersFragment.this.getContext(), remaindersMap.get(username));
        recyclerViewRemainders.setAdapter(remainderAdapter);
        recyclerViewRemainders.setLayoutManager(new LinearLayoutManager(RemaindersFragment.this.getContext()));

        // Setting swipe to delete
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(remainderAdapter, username));
        itemTouchHelper.attachToRecyclerView(recyclerViewRemainders);

        // Click to edit remainder
        recyclerViewRemainders.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(View view, int position) {
                detailsFragment = new DetailsFragment();
                // Get remainder info to pass to fragment
                chosenRemHeader = remainderAdapter.getItem(position).getHeader();
                chosenRemDescription = remainderAdapter.getItem(position).getDescription();
                chosenYear = remainderAdapter.getItem(position).getYear();
                chosenMonth = remainderAdapter.getItem(position).getMonth();
                chosenDay = remainderAdapter.getItem(position).getDayOfMonth();
                chosenHour = remainderAdapter.getItem(position).getHour();
                chosenMinutes = remainderAdapter.getItem(position).getMinutes();

                // Create a new transaction
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // Pass remainder
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

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_container, detailsFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

                // Set toolbar properties
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Remainder");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                // Set fab invisible
                MainActivity.addFab.setVisibility(View.GONE);
            }
        }));

        // Inflate the layout for this fragment
        return remaindersView;
    }

    @Override
    public void onItemClick(View view, int position) {}
}

