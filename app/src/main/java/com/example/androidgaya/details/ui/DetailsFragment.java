package com.example.androidgaya.details.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.example.androidgaya.Navigator;
import com.example.androidgaya.R;
import com.example.androidgaya.reminders.ui.RemindersFragment;
import com.example.androidgaya.repositories.Reminder;
import com.example.androidgaya.repositories.reminder.RemindersRepository;

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
    private DatePickerDialog datePickerDialog;
    private EditText reminderHeaderET;
    private EditText reminderDescriptionET;
    private static Calendar currentTime;
    private static Calendar chosenTime;
    private static String chosenDayStr;
    private static String dateNum;
    private static String reminderHeader = "";
    private static String reminderDescription = "";
    private static String dateWords;
    private static String todayDateNum;
    private static String username = "";
    private static String reminderId = "";
    private boolean isNewFlag = true;
    SharedPreferences prefs;
    Navigator navigator = new Navigator();
    SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
    DateFormat wordsFormat = new SimpleDateFormat("EEE, MMM d", java.util.Locale.getDefault());
    DateFormat dayFormat = new SimpleDateFormat("EEEE", java.util.Locale.getDefault());

    public DetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getUsername();
        init(view);
        updateCurrentTime();
        updateChosenTimeToCurrent();
        setDetailesOnScreen();

        timePicker.setOnTimeChangedListener((timePicker, hourOfDay, minute) -> {
            addDayIfTimePassed(hourOfDay, minute);
            setNewTime(hourOfDay, minute);
        });

        dateButton.setOnClickListener(view1 -> {
            datePickerDialog =
                    new DatePickerDialog(DetailsFragment.this.getContext(), (datePicker, year, month, dayOfMonth) ->
                            setNewDate(year, month, dayOfMonth), chosenTime.get(Calendar.YEAR),
                            chosenTime.get(Calendar.MONTH) - 1, chosenTime.get(Calendar.DATE));
            setMinDate();
            datePickerDialog.show();
        });
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

    public void updateChosenTimeToCurrent() {
        chosenTime = Calendar.getInstance();
        chosenTime.add(Calendar.MONTH, 1);
    }

    public String getDateString(Calendar calendar) {
        return calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) +
                "/" + calendar.get(Calendar.YEAR);
    }

    public boolean isInputValid() {
        return reminderHeaderET.getText().toString().trim().length() != 0;
    }

    public Reminder createReminderFromInput() {
        setUpdatedDetails();
        return new Reminder(reminderId, reminderHeader, reminderDescription,
                chosenTime.get(Calendar.HOUR_OF_DAY), chosenTime.get(Calendar.MINUTE),
                chosenDayStr, chosenTime.get(Calendar.YEAR), chosenTime.get(Calendar.MONTH),
                chosenTime.get(Calendar.DATE));
    }

    public void setUpdatedDetails() {
        reminderHeader = reminderHeaderET.getText().toString();
        reminderDescription = reminderDescriptionET.getText().toString();
    }

    @SuppressLint("RestrictedApi")
    public void save() {
        if (isInputValid()) {
            if (isTimeValid()) {
                if (isNewFlag) {
                    setNewID();
                    RemindersRepository.getInstance().addReminder(createReminderFromInput(), username);
                    makeToast(getString(R.string.add_msg));
                } else {
                    RemindersRepository.getInstance().editReminder(createReminderFromInput(), username);
                    makeToast(getString(R.string.update_msg));
                }
                RemindersFragment remindersFragment = new RemindersFragment();
                navigator.changeFragment(remindersFragment, getContext());
            } else {
                makeToast(getString(R.string.select_valid_time_msg));
            }
        } else {
            makeToast(getString(R.string.enter_name_msg));
        }
    }

    public void setDetailesOnScreen() {
        todayDateNum = getDateString(currentTime);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isNewFlag = false;
            navigator.changeToolbar(getString(R.string.edit_rem), true, getContext());
            reminderId = arguments.getString(getString(R.string.id), "");
            Reminder reminder = RemindersRepository.getInstance().getReminderByID(username, reminderId);
            reminderHeader = reminder.getHeader();
            reminderDescription = reminder.getDescription();
            chosenTime.set(reminder.getYear(), reminder.getMonth(), reminder.getDayOfMonth(),
                    reminder.getHour(), reminder.getMinutes());
            reminderHeaderET.setText(reminderHeader);
            reminderDescriptionET.setText(reminderDescription);
        } else {
            navigator.changeToolbar(getString(R.string.add_rem), true, getContext());
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
    }

    public void init(View view) {
        reminderHeaderET = view.findViewById(R.id.rem_name_et);
        reminderDescriptionET = view.findViewById(R.id.rem_description_et);
        dateTV = view.findViewById(R.id.date_tv);
        dateButton = view.findViewById(R.id.button_date);
        timePicker = view.findViewById(R.id.time_picker);
    }

    public void getUsername() {
        prefs = getContext().getSharedPreferences(getString(R.string.user_details_sp), Context.MODE_PRIVATE);
        username = prefs.getString(getString(R.string.username), "");
    }

    public void makeToast(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setNewID() {
        UUID uuid = UUID.randomUUID();
        reminderId = uuid.toString();
    }

    public void setNewTime(int hourOfDay, int minute) {
        chosenTime.set(chosenTime.get(Calendar.YEAR), chosenTime.get(Calendar.MONTH),
                chosenTime.get(Calendar.DATE), hourOfDay, minute);
    }

    public void setNewDate(int year, int month, int dayOfMonth) {
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
    }

    public void addDayIfTimePassed(int hourOfDay, int minute) {
        if (isChosenTimeAndDayPassed(hourOfDay, minute)) {
            makeToast(getString(R.string.tomorrow_msg));
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
    }

    public void setMinDate() {
        if (isChosenTimePassed()) {
            datePickerDialog.getDatePicker().setMinDate(
                    (System.currentTimeMillis() + 24 * 60 * 60 * 1000) - 1000);
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
    }
}