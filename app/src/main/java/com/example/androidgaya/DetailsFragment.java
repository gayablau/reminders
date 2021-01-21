package com.example.androidgaya;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailsFragment extends Fragment {

    final int NO_EDIT_FLAG = -1;
    private TextView dateTV;
    private static EditText remainderHeaderET;
    private static EditText remainderDescriptionET;
    private static Calendar calendar;
    private Button dateButton;
    private Toolbar toolbar;
    private TimePicker timePicker;
    private static int position;
    private static int chosenYear = 1970;
    private static int chosenMonth = 1;
    private static int chosenDay = 1;
    private static int chosenHour = 00;
    private static int chosenMinutes = 00;
    private static String chosenDayStr = "THURSDAY";
    private static String dateNum = "1/1/1970";
    private static String remainderHeader = "";
    private static String remainderDescription = "";
    private static String dateWords;
    private static String todayDateNum = "1/1/1970";
    private boolean isEdit = false;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addC
    }

    public boolean isTimeValid() {
        // Returns true if time is valid, else false
        calendar = Calendar.getInstance();
        todayDateNum = calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR);
        if (!(dateNum.compareTo(todayDateNum) == 0 &&
                (chosenHour < calendar.get(Calendar.HOUR_OF_DAY) ||
                (chosenHour == calendar.get(Calendar.HOUR_OF_DAY) &&
                chosenMinutes <= calendar.get(Calendar.MINUTE))))) {
            return true;
        }
        return false;
    }

    public boolean isInputValid() {
        // Returns true if input is valid (name is not empty), else false
        return remainderHeaderET.getText().toString().trim().length() != 0;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public int getPosition() {
        return position;
    }

    public Remainder createRemainderFromInput() {
        setUpdatedDetails();
        // Returns a remainder based on current input
        return new Remainder(remainderHeader, remainderDescription, chosenHour, chosenMinutes, chosenDayStr, chosenYear, chosenMonth, chosenDay);
    }

    public void clearInput() {
        // Clear Input
        remainderHeaderET.setText("");
        remainderDescriptionET.setText("");
    }

    public void setUpdatedDetails() {
        remainderHeader = remainderHeaderET.getText().toString();
        remainderDescription = remainderDescriptionET.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.fragment_details, container, false);
        remainderHeaderET = (EditText) detailsView.findViewById(R.id.editTextRemainderName);
        remainderDescriptionET = (EditText) detailsView.findViewById(R.id.editTextDescription);
        dateTV = (TextView) detailsView.findViewById(R.id.textViewDate);
        toolbar = (Toolbar) detailsView.findViewById(R.id.toolbar);
        dateButton = (Button) detailsView.findViewById(R.id.buttonDate);
        timePicker = (TimePicker) detailsView.findViewById(R.id.timePicker);
        toolbar = (Toolbar) detailsView.findViewById(R.id.toolbar);
        position = NO_EDIT_FLAG;
        setHasOptionsMenu(true);
        return detailsView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Set menu save
        menu.clear();
        inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        // if editing, get chosen remainder's details. else get current time.
        Bundle arguments = getArguments();
        calendar = Calendar.getInstance();
        todayDateNum = calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR);
        if (arguments != null) {
            remainderHeader = arguments.getString("Header", "");
            remainderDescription = arguments.getString("Description", "");
            position = arguments.getInt("Position", -1);
            chosenYear = arguments.getInt("Year", 1970);
            chosenMonth = arguments.getInt("Month", 1);
            chosenDay = arguments.getInt("Day", 1);
            chosenHour = arguments.getInt("Hour", 00);
            chosenMinutes = arguments.getInt("Minutes", 00);
            remainderHeaderET.setText(remainderHeader);
            remainderDescriptionET.setText(remainderDescription);
        }
        else {
            // Get and set current time
            chosenYear = calendar.get(Calendar.YEAR);
            chosenMonth = calendar.get(Calendar.MONTH) + 1;
            chosenDay = calendar.get(Calendar.DATE);
            chosenHour = calendar.get(Calendar.HOUR_OF_DAY);
            chosenMinutes = calendar.get(Calendar.MINUTE);
        }

        dateNum = chosenDay + "/" + chosenMonth + "/" + chosenYear;
        SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = fullFormat.parse(dateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
        dateWords = wordsFormat.format(date);
        dateTV.setText(dateWords);
        DateFormat dayFormat = new SimpleDateFormat("EEE");
        chosenDayStr = dayFormat.format(date);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(chosenHour);
        timePicker.setCurrentMinute(chosenMinutes);

        // Pick time
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar = Calendar.getInstance();
                if (dateNum.compareTo(todayDateNum) == 0 &&
                        (hourOfDay < calendar.get(Calendar.HOUR_OF_DAY) ||
                        (hourOfDay == calendar.get(Calendar.HOUR_OF_DAY) &&
                        minute <= calendar.get(Calendar.MINUTE)))) {
                    // Set date to tomorrow according to selected time
                    Toast.makeText(DetailsFragment.this.getContext(),
                            "past time selected. setting remainder date to tomorrow",
                            Toast.LENGTH_LONG).show();
                    Date tomorrowDate = new Date();
                    Calendar tomorrowCalendar = Calendar.getInstance();
                    tomorrowCalendar.setTime(tomorrowDate);
                    tomorrowCalendar.add(Calendar.DATE, 1);
                    chosenYear = tomorrowCalendar.get(Calendar.YEAR);
                    chosenMonth = tomorrowCalendar.get(Calendar.MONTH) + 1;
                    chosenDay = tomorrowCalendar.get(Calendar.DATE);
                    dateNum = chosenDay + "/" + chosenMonth + "/" + chosenYear;
                    SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = fullFormat.parse(dateNum);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
                    dateWords = wordsFormat.format(date);
                    dateTV.setText(dateWords);
                    DateFormat dayFormat = new SimpleDateFormat("EEE");
                    chosenDayStr = dayFormat.format(date);
                }
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
                        Date date = null;
                        try {
                            date = fullFormat.parse(dateNum);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
                        dateWords = wordsFormat.format(date);
                        dateTV.setText(dateWords);
                        DateFormat dayFormat = new SimpleDateFormat("EEE");
                        chosenDayStr = dayFormat.format(date);
                        chosenYear = year;
                        chosenMonth = month + 1;
                        chosenDay = dayOfMonth;
                    }
                }, chosenYear, chosenMonth - 1, chosenDay);
                // Set min date according to selected time
                if ((chosenHour < calendar.get(Calendar.HOUR_OF_DAY) ||
                        (chosenHour == calendar.get(Calendar.HOUR_OF_DAY) &&
                        chosenMinutes <= calendar.get(Calendar.MINUTE)))) {
                    datePickerDialog.getDatePicker().setMinDate((System.currentTimeMillis() + 86400 * 1000) - 1000);
                }
                else {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
                datePickerDialog.show();
            }
        });
    }
}