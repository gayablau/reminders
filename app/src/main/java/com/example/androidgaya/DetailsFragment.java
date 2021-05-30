package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
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
    private Button dateButton;
    private TimePicker timePicker;
    private EditText remainderHeaderET;
    private EditText remainderDescriptionET;
    private static Calendar calendar;
    private static int position;
    private static int chosenYear = 1970;
    private static int chosenMonth = 1;
    private static int chosenDay = 1;
    private static int chosenHour = 0;
    private static int chosenMinutes = 0;
    private static String chosenDayStr = "THURSDAY";
    private static String dateNum = "1/1/1970";
    private static String remainderHeader = "";
    private static String remainderDescription = "";
    private static String dateWords;
    private static String todayDateNum = "1/1/1970";
    private static String username = "";
    SharedPreferences prefs;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = DetailsFragment.this.getContext()
                .getSharedPreferences(getString(R.string.userdetails), Context.MODE_PRIVATE);
        username = prefs.getString(getString(R.string.username), "");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean isTimeValid() {
        calendar = Calendar.getInstance();
        todayDateNum = calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR);
        return !(dateNum.compareTo(todayDateNum) == 0 &&
                (chosenHour < calendar.get(Calendar.HOUR_OF_DAY) ||
                        (chosenHour == calendar.get(Calendar.HOUR_OF_DAY) &&
                                chosenMinutes < calendar.get(Calendar.MINUTE))));
    }

    public boolean isTimePast(Calendar calendar) {
        return ((chosenHour < calendar.get(Calendar.HOUR_OF_DAY) ||
                (chosenHour == calendar.get(Calendar.HOUR_OF_DAY) &&
                        chosenMinutes < calendar.get(Calendar.MINUTE))));
    }

    public boolean isInputValid() {
        return remainderHeaderET.getText().toString().trim().length() != 0;
    }

    public int getPosition() {
        return position;
    }

    public Remainder createRemainderFromInput() {
        setUpdatedDetails();
        return new Remainder(remainderHeader, remainderDescription, chosenHour, chosenMinutes,
                chosenDayStr, chosenYear, chosenMonth, chosenDay);
    }

    public void setUpdatedDetails() {
        remainderHeader = remainderHeaderET.getText().toString();
        remainderDescription = remainderDescriptionET.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.fragment_details, container, false);
        remainderHeaderET = detailsView.findViewById(R.id.edit_text_remainder_name);
        remainderDescriptionET = detailsView.findViewById(R.id.edit_text_description);
        dateTV = detailsView.findViewById(R.id.text_view_date);
        dateButton = detailsView.findViewById(R.id.button_date);
        timePicker = detailsView.findViewById(R.id.time_picker);
        position = NO_EDIT_FLAG;

        return detailsView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        if (isInputValid()) {
            if (isTimeValid()) {
                if (getPosition() == -1) {
                    RemaindersBase.get().addRemainder(createRemainderFromInput(), username);
                    Toast.makeText(getActivity(), "remainder added",
                            Toast.LENGTH_SHORT).show();
                } else {
                    RemaindersBase.get().editRemainder(getPosition(), createRemainderFromInput(), username);
                    Toast.makeText(getActivity(), "remainder updated",
                            Toast.LENGTH_SHORT).show();
                }
                RemaindersFragment remaindersFragment = new RemaindersFragment();
                ((MainActivity) getActivity()).changeFragment(remaindersFragment);
            } else {
                Toast.makeText(getActivity(), "please select a valid time for your remainder", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "please enter a name for your remainder", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        calendar = Calendar.getInstance();
        todayDateNum = calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR);
        Bundle arguments = getArguments();
        if (arguments != null) {
            remainderHeader = arguments.getString("Header", "");
            remainderDescription = arguments.getString("Description", "");
            position = arguments.getInt("Position", -1);
            chosenYear = arguments.getInt("Year", calendar.get(Calendar.YEAR));
            chosenMonth = arguments.getInt("Month", calendar.get(Calendar.MONTH) + 1);
            chosenDay = arguments.getInt("Day", calendar.get(Calendar.DATE));
            chosenHour = arguments.getInt("Hour", calendar.get(Calendar.HOUR_OF_DAY));
            chosenMinutes = arguments.getInt("Minutes", calendar.get(Calendar.MINUTE));
            remainderHeaderET.setText(remainderHeader);
            remainderDescriptionET.setText(remainderDescription);
        } else {
            chosenYear = calendar.get(Calendar.YEAR);
            chosenMonth = calendar.get(Calendar.MONTH) + 1;
            chosenDay = calendar.get(Calendar.DATE);
            chosenHour = calendar.get(Calendar.HOUR_OF_DAY);
            chosenMinutes = calendar.get(Calendar.MINUTE);
        }
        dateNum = chosenDay + "/" + chosenMonth + "/" + chosenYear;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = fullFormat.parse(dateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
        dateWords = wordsFormat.format(date);
        dateTV.setText(dateWords);
        @SuppressLint("SimpleDateFormat") DateFormat dayFormat = new SimpleDateFormat("EEEE");
        chosenDayStr = dayFormat.format(date);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(chosenHour);
        timePicker.setCurrentMinute(chosenMinutes);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar = Calendar.getInstance();
                if (dateNum.compareTo(todayDateNum) == 0 &&
                        (hourOfDay < calendar.get(Calendar.HOUR_OF_DAY) ||
                                (hourOfDay == calendar.get(Calendar.HOUR_OF_DAY) &&
                                        minute < calendar.get(Calendar.MINUTE)))) {
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
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = fullFormat.parse(dateNum);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    @SuppressLint("SimpleDateFormat") DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
                    dateWords = wordsFormat.format(date);
                    dateTV.setText(dateWords);
                    @SuppressLint("SimpleDateFormat") DateFormat dayFormat = new SimpleDateFormat("EEEE");
                    chosenDayStr = dayFormat.format(date);
                }
                chosenHour = hourOfDay;
                chosenMinutes = minute;
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog =
                        new DatePickerDialog(DetailsFragment.this.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateNum = dayOfMonth + "/" + (month + 1) + "/" + year;
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = null;
                                try {
                                    date = fullFormat.parse(dateNum);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                @SuppressLint("SimpleDateFormat") DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
                                dateWords = wordsFormat.format(date);
                                dateTV.setText(dateWords);
                                @SuppressLint("SimpleDateFormat") DateFormat dayFormat = new SimpleDateFormat("EEEE");
                                chosenDayStr = dayFormat.format(date);
                                chosenYear = year;
                                chosenMonth = month + 1;
                                chosenDay = dayOfMonth;
                            }
                        }, chosenYear, chosenMonth - 1, chosenDay);
                if ((chosenHour < calendar.get(Calendar.HOUR_OF_DAY) ||
                        (chosenHour == calendar.get(Calendar.HOUR_OF_DAY) &&
                                chosenMinutes < calendar.get(Calendar.MINUTE)))) {
                    datePickerDialog.getDatePicker().setMinDate(
                            (System.currentTimeMillis() + 86400 * 1000) - 1000);
                } else {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
                datePickerDialog.show();
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        RemaindersFragment remaindersFragment = new RemaindersFragment();
        ((MainActivity) getActivity()).changeFragment(remaindersFragment);
    }
}