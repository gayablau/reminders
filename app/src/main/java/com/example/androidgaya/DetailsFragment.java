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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailsFragment extends Fragment {

    private TextView dateTV;
    private static EditText remainderHeaderET;
    private static EditText remainderDescriptionET;
    private Button dateButton;
    private Toolbar toolbar;
    private TimePicker timePicker;
    private MenuItem saveMenu;
    private static int position = -1;
    private int chosenYear = 1970;
    private int chosenMonth = 1;
    private int chosenDay = 1;
    private int chosenHour = 00;
    private int chosenMinutes = 00;
    private String chosenDayStr = "THURSDAY";
    private String dateNum = "1/1/1970";
    private static String remainderHeader = "";
    private static String remainderDescription = "";
    private String dateWords;
    private boolean isEdit = false;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean isInputValid() {
        // Returns true if input is valid, else false
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
        if (chosenMinutes < 10) {
            return new Remainder(remainderHeader, remainderDescription, chosenHour + ":0" + chosenMinutes, dateNum, chosenDayStr);
        }
        return new Remainder(remainderHeader, remainderDescription,chosenHour + ":" + chosenMinutes, dateNum, chosenDayStr);
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
        position = -1;
        setHasOptionsMenu(true);

      /*  remainderHeaderET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                remainderHeader = remainderHeaderET.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        remainderDescriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                remainderDescription = remainderDescriptionET.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        return detailsView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null) {
            // get and show chosen remainder's details
            remainderHeader = arguments.getString("Header");
            remainderDescription = arguments.getString("Description");
            position = arguments.getInt("Position");
            remainderHeaderET.setText(remainderHeader);
            remainderDescriptionET.setText(remainderDescription);
            // add set time ********
        }
        else {
        }

        // Get and set current time
        Calendar calendar = Calendar.getInstance();
        chosenYear = calendar.get(Calendar.YEAR);
        chosenMonth = calendar.get(Calendar.MONTH) + 1;
        chosenDay = calendar.get(Calendar.DATE);
        chosenHour = calendar.get(Calendar.HOUR_OF_DAY) + 2;
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
    }
}