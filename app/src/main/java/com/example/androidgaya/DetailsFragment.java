package com.example.androidgaya;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailsFragment extends Fragment {

    private TextView dateTV;
    private EditText remainderNameET;
    private EditText remainderDescriptionET;
    private Button dateButton;
    private Toolbar toolbar;
    private TimePicker timePicker;
    int chosenYear = 1970;
    int chosenMonth = 1;
    int chosenDay = 1;
    int chosenHour = 00;
    int chosenMinutes = 00;
    String chosenDayStr = "THURSDAY";
    String dateNum = "1/1/1970";
    String dateWords;

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
        if (chosenMinutes < 10) {
            return new Remainder(remainderNameET.getText().toString(), remainderDescriptionET.getText().toString(),chosenHour + ":0" + chosenMinutes, dateNum, chosenDayStr);
        }
        return new Remainder(remainderNameET.getText().toString(), remainderDescriptionET.getText().toString(),chosenHour + ":" + chosenMinutes, dateNum, chosenDayStr);
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
        timePicker = (TimePicker) detailsView.findViewById(R.id.timePicker);

        // Get and set current time
        Calendar calendar = Calendar.getInstance();
        chosenYear = calendar.get(Calendar.YEAR);
        chosenMonth = calendar.get(Calendar.MONTH) + 1;
        chosenDay = calendar.get(Calendar.DATE);
        chosenHour = calendar.get(Calendar.HOUR_OF_DAY);
        chosenMinutes = calendar.get(Calendar.MINUTE);
        dateNum = chosenDay + "/" + chosenMonth + "/" + chosenYear;
        SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dt1 = null;
        try {
            dt1 = fullFormat.parse(dateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
        dateWords = wordsFormat.format(dt1);
        dateTV.setText(dateWords);
        DateFormat dayFormat = new SimpleDateFormat("EEE");
        chosenDayStr = dayFormat.format(dt1);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(chosenHour);
        timePicker.setCurrentMinute(chosenMinutes);

        // Pick time
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                chosenHour = hourOfDay;
                chosenMinutes = minute;
            }
        });

        // Pick a date
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsFragment.this.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateNum = dayOfMonth + "/" + (month + 1) + "/" + year;
                        SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date dt1 = null;
                        try {
                            dt1 = fullFormat.parse(dateNum);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
                        dateWords = wordsFormat.format(dt1);
                        dateTV.setText(dateWords);
                        DateFormat dayFormat = new SimpleDateFormat("EEE");
                        chosenDayStr = dayFormat.format(dt1);
                        chosenYear = year;
                        chosenMonth = month + 1;
                        chosenDay = dayOfMonth;
                    }
                }, chosenYear, chosenMonth - 1, chosenDay);
                datePickerDialog.show();
            }
        });
        return detailsView;
    }
}