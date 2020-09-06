package com.example.androidgaya;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    private TextView dateTV;
    private EditText remainderNameET;
    private EditText remainderDescriptionET;
    private Button dateButton;
    private Toolbar toolbar;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean isInputValid() {
        // Returns true if input is valid, else false
        if (remainderNameET.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    public Remainder createRemainderFromInput() {
        // Returns a remainder based on current input
        return new Remainder(remainderNameET.getText().toString(),remainderDescriptionET.getText().toString(),"00:00","00/00/00","SUN");
    }

    public void clearInput() {
        // Clear Input
        remainderNameET.setText("");
        remainderDescriptionET.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.fragment_details, container, false);
        remainderNameET = (EditText) detailsView.findViewById(R.id.editTextRemainderName);
        remainderDescriptionET = (EditText) detailsView.findViewById(R.id.editTextDescription);
        dateTV = (TextView) detailsView.findViewById(R.id.textViewDate);
        toolbar = (Toolbar) detailsView.findViewById(R.id.toolbar);
        dateButton = (Button) detailsView.findViewById(R.id.buttonDate);
        return detailsView;
    }
}