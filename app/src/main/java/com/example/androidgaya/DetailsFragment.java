package com.example.androidgaya;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class DetailsFragment extends Fragment {

    private TextView dateTV;
    private Button dateButton;
    private TimePicker timePicker;
    private EditText remainderHeaderET;
    private EditText remainderDescriptionET;
    private static Calendar currentTime;
    private static Calendar chosenTime;
    private static String chosenDayStr;
    private static String dateNum;
    private static String remainderHeader = "";
    private static String remainderDescription = "";
    private static String dateWords;
    private static String todayDateNum;
    private static String username = "";
    private static String remainderId = "";
    private boolean isNewFlag = true;
    SharedPreferences prefs;
    SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d");
    DateFormat dayFormat = new SimpleDateFormat("EEEE");

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
        updateCurrentTime();
        return !(isChosenTimeAndDayPassed(chosenTime.get(Calendar.HOUR_OF_DAY),
                chosenTime.get(Calendar.MINUTE)));
    }

    public boolean isChosenTimePassed() {
        updateCurrentTime();
        return ((chosenTime.get(Calendar.HOUR_OF_DAY) < currentTime.get(Calendar.HOUR_OF_DAY) ||
                (chosenTime.get(Calendar.HOUR_OF_DAY) == currentTime.get(Calendar.HOUR_OF_DAY) &&
                        chosenTime.get(Calendar.MINUTE) < currentTime.get(Calendar.MINUTE))));
    }

    public boolean isChosenTimeAndDayPassed(int hourOfDay, int minute) {
        updateCurrentTime();
        return dateNum.compareTo(todayDateNum) == 0 &&
                (hourOfDay < currentTime.get(Calendar.HOUR_OF_DAY) ||
                        (hourOfDay == currentTime.get(Calendar.HOUR_OF_DAY) &&
                                minute < currentTime.get(Calendar.MINUTE)));
    }

    public void updateCurrentTime() {
        currentTime = Calendar.getInstance();
        currentTime.add(Calendar.MONTH, 1);
    }

    public String getDateString(Calendar calendar) {
        return calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) +
                "/" + calendar.get(Calendar.YEAR);
    }

    public boolean isInputValid() {
        return remainderHeaderET.getText().toString().trim().length() != 0;
    }

    public Remainder createRemainderFromInput() {
        setUpdatedDetails();
        return new Remainder(remainderId, remainderHeader, remainderDescription,
                chosenTime.get(Calendar.HOUR_OF_DAY), chosenTime.get(Calendar.MINUTE),
                chosenDayStr, chosenTime.get(Calendar.YEAR), chosenTime.get(Calendar.MONTH),
                chosenTime.get(Calendar.DATE));
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
                if (isNewFlag) {
                    UUID uuid = UUID.randomUUID();
                    remainderId = uuid.toString();
                    RemaindersBase.get().addRemainder(createRemainderFromInput(), username);
                    Toast.makeText(getActivity(), "remainder added",
                            Toast.LENGTH_SHORT).show();
                } else {
                    RemaindersBase.get().editRemainder(createRemainderFromInput(), username);
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
        updateCurrentTime();
        chosenTime = Calendar.getInstance();
        chosenTime.add(Calendar.MONTH, 1);
        todayDateNum = getDateString(currentTime);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isNewFlag = false;
            remainderId = arguments.getString("Id", "");
            Remainder remainder = RemaindersBase.get().getRemainderByID(username, remainderId);
            remainderHeader = remainder.getHeader();
            remainderDescription = remainder.getDescription();
            chosenTime.set(remainder.getYear(), remainder.getMonth(), remainder.getDayOfMonth(),
                    remainder.getHour(), remainder.getMinutes());
            remainderHeaderET.setText(remainderHeader);
            remainderDescriptionET.setText(remainderDescription);
        } else {
            chosenTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH),
                    currentTime.get(Calendar.DATE), currentTime.get(Calendar.HOUR_OF_DAY),
                    currentTime.get(Calendar.MINUTE));
        }
        dateNum = getDateString(chosenTime);
        Date date = null;
        try {
            date = fullFormat.parse(dateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateWords = wordsFormat.format(date);
        dateTV.setText(dateWords);
        chosenDayStr = dayFormat.format(date);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(chosenTime.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(chosenTime.get(Calendar.MINUTE));

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
             if (isChosenTimeAndDayPassed(hourOfDay, minute)) {
                Toast.makeText(DetailsFragment.this.getContext(),
                        "past time selected. setting remainder date to tomorrow",
                        Toast.LENGTH_LONG).show();
                chosenTime.add(Calendar.DATE, 1);
                dateNum = getDateString(chosenTime);
                Date date1 = null;
                try {
                    date1 = fullFormat.parse(dateNum);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateWords = wordsFormat.format(date1);
                dateTV.setText(dateWords);
                chosenDayStr = dayFormat.format(date1);
            }
            chosenTime.set(chosenTime.get(Calendar.YEAR), chosenTime.get(Calendar.MONTH),
                    chosenTime.get(Calendar.DATE), hourOfDay, minute);
        });

        dateButton.setOnClickListener(v -> {
            final DatePickerDialog datePickerDialog =
                    new DatePickerDialog(DetailsFragment.this.getContext(), (view, year, month, dayOfMonth) -> {
                        dateNum = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Date date12 = null;
                        try {
                            date12 = fullFormat.parse(dateNum);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dateWords = wordsFormat.format(date12);
                        dateTV.setText(dateWords);
                        chosenDayStr = dayFormat.format(date12);
                        chosenTime.set(year, month + 1, dayOfMonth,
                                chosenTime.get(Calendar.HOUR_OF_DAY), chosenTime.get(Calendar.MINUTE));
                    }, chosenTime.get(Calendar.YEAR), chosenTime.get(Calendar.MONTH) - 1,
                            chosenTime.get(Calendar.DATE));
            if (isChosenTimePassed()) {
                datePickerDialog.getDatePicker().setMinDate(
                        (System.currentTimeMillis() + 24 * 60 * 60 * 1000) - 1000);
            } else {
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
            datePickerDialog.show();
        });
        
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
                return true;
            }
            return false;
        });
    }

    public void onBackPressed() {
        RemaindersFragment remaindersFragment = new RemaindersFragment();
        ((MainActivity) getActivity()).changeFragment(remaindersFragment);
    }
}