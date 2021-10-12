package com.example.androidgaya.details.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.androidgaya.factory.ViewModelFactory;
import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.repositories.di.AppDataGetter;
import com.example.androidgaya.repositories.models.ReminderEntity;
import com.example.androidgaya.repositories.socket.SocketRepo;
import com.example.androidgaya.util.MainNavigator;
import com.example.androidgaya.R;
import com.example.androidgaya.details.viewmodel.DetailsViewModel;
import com.example.androidgaya.util.NotificationUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.inject.Inject;

public class DetailsFragment extends Fragment {
    private static final String ID_KEY = "id";
    private TextView dateTV;
    private Button dateButton;
    private TimePicker timePicker;
    private DatePickerDialog datePickerDialog;
    private EditText reminderHeaderET;
    private EditText reminderDescriptionET;
    private MenuItem menuItem;
    private Calendar currentTime;
    private Calendar chosenTime;
    private String chosenDayStr;
    private String chosenDateNum;
    private String chosenReminderHeader = "";
    private String chosenReminderDescription = "";
    private String chosenDateWords;
    private String todayDateNum;
    private int reminderId = 0;
    private long createdAt = 0;
    private boolean isNewFlag = true;
    private DetailsViewModel viewModel;
    private MainNavigator nav;
    private SimpleDateFormat fullFormat;
    private DateFormat wordsFormat;
    private DateFormat dayFormat;
    private boolean isNotified = false;
    ReminderEntity reminderEntity;

    @Inject
    ViewModelFactory factory;

    public DetailsFragment() {
    }

    public static DetailsFragment getInstance(int id) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ID_KEY, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static DetailsFragment getInstance() {
        return new DetailsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((AppDataGetter) getActivity().getApplicationContext()).getAppComponent().injectDetails(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initViews(view);
        initVariables();
        updateCurrentTime();
        updateChosenTimeToCurrent();
        setDetailsOnScreen();
        initListeners();
    }

    public void initListeners() {
        initTimePicker();
        initDatePickerDialog();
    }

    public void initVariables() {
        fullFormat = new SimpleDateFormat(getString(R.string.full_date), java.util.Locale.getDefault());
        wordsFormat = new SimpleDateFormat(getString(R.string.words_date), java.util.Locale.getDefault());
        dayFormat = new SimpleDateFormat(getString(R.string.day_date), java.util.Locale.getDefault());
    }

    public void initTimePicker() {
        timePicker.setOnTimeChangedListener((timePicker, hourOfDay, minute) -> {
            addDayIfTimePassed(hourOfDay, minute);
            setNewTime(hourOfDay, minute);
        });
    }

    public void initDatePickerDialog() {
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
        menuItem = menu.findItem(R.id.action_save);
        if (isNewFlag) {
            menuItem.setTitle(R.string.add);
        } else {
            menuItem.setTitle(R.string.save);
        }
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
        return chosenDateNum.compareTo(todayDateNum) == 0 &&
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
        Date date = calendar.getTime();
        Calendar updatedCal = Calendar.getInstance();
        updatedCal.setTime(date);
        updatedCal.add(Calendar.MONTH, -1);
        date = updatedCal.getTime();
        return fullFormat.format(date);
    }

    public boolean isInputValid() {
        return reminderHeaderET.getText().toString().trim().length() != 0;
    }

    public ReminderEntity createReminderFromInput() {
        setUpdatedDetails();
        if (isNewFlag) {
            reminderId = new Random(System.currentTimeMillis()).nextInt(10000);
        }
        reminderEntity = new ReminderEntity(reminderId,
                chosenReminderHeader,
                chosenReminderDescription,
                viewModel.getUserId(),
                chosenTime.getTimeInMillis(),
                createdAt);
        return reminderEntity;
    }

    public void setUpdatedDetails() {
        chosenReminderHeader = reminderHeaderET.getText().toString();
        chosenReminderDescription = reminderDescriptionET.getText().toString();
    }

    @SuppressLint("RestrictedApi")
    public void save() {
        if (isInputValid()) {
            if (isTimeValid()) {
                resetSeconds();
                if (isNewFlag) {
                    viewModel.addReminder(createReminderFromInput());
                    if (!isNotified) {
                        isNotified = true;
                    }
                    makeToast(getString(R.string.add_msg));
                } else {
                    viewModel.editReminder(createReminderFromInput());
                    makeToast(getString(R.string.update_msg));
                }
                new NotificationUtils().setNotification(chosenTime.getTimeInMillis(),
                        getActivity().getApplicationContext(),
                        chosenReminderHeader,
                        chosenReminderDescription,
                        reminderId);
                nav.toRemindersFragment();
            } else {
                makeToast(getString(R.string.select_valid_time_msg));
            }
        } else {
            makeToast(getString(R.string.enter_name_msg));
        }
    }

    private void resetSeconds() {
        chosenTime.set(chosenTime.get(Calendar.YEAR),
                chosenTime.get(Calendar.MONTH),
                chosenTime.get(Calendar.DATE),
                chosenTime.get(Calendar.HOUR_OF_DAY),
                chosenTime.get(Calendar.MINUTE),
                0);
    }

    public void setDetailsOnScreen() {
        todayDateNum = getDateString(currentTime);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isNewFlag = false;
            ((MainActivityInterface) getActivity()).changeToolbar(getString(R.string.edit_rem), true);
            reminderId = arguments.getInt(ID_KEY, 0);
            try {
                ReminderEntity reminderEntity = viewModel.getReminderByID(reminderId);
                chosenReminderHeader = reminderEntity.getHeader();
                chosenReminderDescription = reminderEntity.getDescription();
                createdAt = reminderEntity.getCreatedAt();
                chosenTime.set(reminderEntity.getYear(),
                        reminderEntity.getMonth(),
                        reminderEntity.getDayOfMonth(),
                        reminderEntity.getHour(),
                        reminderEntity.getMinutes());
                reminderHeaderET.setText(chosenReminderHeader);
                reminderDescriptionET.setText(chosenReminderDescription);
            } catch (Exception ex) {
                makeToast(getString(R.string.msg_rem_not_found));
                nav.toRemindersFragment();
            }
        } else {
            ((MainActivityInterface) getActivity()).changeToolbar(getString(R.string.add_rem), true);
            chosenTime.set(currentTime.get(Calendar.YEAR),
                    currentTime.get(Calendar.MONTH),
                    currentTime.get(Calendar.DATE),
                    currentTime.get(Calendar.HOUR_OF_DAY),
                    currentTime.get(Calendar.MINUTE));
        }
        chosenDateNum = getDateString(chosenTime);
        Date date = null;
        try {
            date = fullFormat.parse(chosenDateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            chosenDateWords = wordsFormat.format(date);
            chosenDayStr = dayFormat.format(date);
        }
        dateTV.setText(chosenDateWords);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(chosenTime.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(chosenTime.get(Calendar.MINUTE));
    }

    public void initViews(View view) {
        reminderHeaderET = view.findViewById(R.id.rem_name_et);
        reminderDescriptionET = view.findViewById(R.id.rem_description_et);
        dateTV = view.findViewById(R.id.date_tv);
        dateButton = view.findViewById(R.id.button_date);
        timePicker = view.findViewById(R.id.time_picker);
        viewModel = new ViewModelProvider(this, factory).get(DetailsViewModel.class);
        nav = ((MainActivityInterface) getActivity()).getNavigator();
    }

    public void makeToast(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setNewTime(int hourOfDay, int minute) {
        chosenTime.set(chosenTime.get(Calendar.YEAR), chosenTime.get(Calendar.MONTH),
                chosenTime.get(Calendar.DATE), hourOfDay, minute);
    }

    public void setNewDate(int year, int month, int dayOfMonth) {
        int currentMonth = month + 1;
        chosenDateNum = dayOfMonth + "/" + currentMonth + "/" + year;
        Date date = null;
        try {
            date = fullFormat.parse(chosenDateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            chosenDateWords = wordsFormat.format(date);
            dateTV.setText(chosenDateWords);
            chosenDayStr = dayFormat.format(date);
        }
        chosenTime.set(year, currentMonth, dayOfMonth,
                chosenTime.get(Calendar.HOUR_OF_DAY), chosenTime.get(Calendar.MINUTE));
    }

    public void addDayIfTimePassed(int hourOfDay, int minute) {
        if (isChosenTimeAndDayPassed(hourOfDay, minute)) {
            makeToast(getString(R.string.tomorrow_msg));
            chosenTime.add(Calendar.DATE, 1);
            chosenDateNum = getDateString(chosenTime);
            Date date1 = null;
            try {
                date1 = fullFormat.parse(chosenDateNum);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date1 != null) {
                chosenDateWords = wordsFormat.format(date1);
                chosenDayStr = dayFormat.format(date1);
            }
            dateTV.setText(chosenDateWords);
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