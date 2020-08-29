package com.example.androidgaya;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RemaindersFragment extends Fragment {

    private RecyclerView recyclerViewRemainders;
    private RecyclerView.Adapter remainderAdapter;
    private RecyclerView.LayoutManager layoutManager;

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

        ArrayList<Remainder> listRemainders = RemaindersBase.get().getListRemainders();

        recyclerViewRemainders = (RecyclerView) remaindersView.findViewById(R.id.recyclerViewRemainders);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerViewRemainders.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(RemaindersFragment.this.getContext());
        recyclerViewRemainders.setLayoutManager(layoutManager);

        // specify an adapter to convert the array to views
        remainderAdapter = new RemainderAdapter(RemaindersFragment.this.getContext(), listRemainders);
        recyclerViewRemainders.setAdapter(remainderAdapter);

        // Inflate the layout for this fragment
        return remaindersView;
    }
}