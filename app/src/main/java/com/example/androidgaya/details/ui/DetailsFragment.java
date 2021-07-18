package com.example.androidgaya.details.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

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

import com.example.androidgaya.main.interfaces.MainActivityInterface;
import com.example.androidgaya.main.notify.NotifyWork;
import com.example.androidgaya.util.MainNavigator;
import com.example.androidgaya.R;
import com.example.androidgaya.details.viewmodel.DetailsViewModel;
import com.example.androidgaya.repositories.models.Reminder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.google.android.material.snackbar.Snackbar.make;

public class DetailsFragment extends Fragment {
    private static final String ID_KEY = "id";
    private static final String NOTIFICATION_ID = "appName_notification_id";
    private static final String REMINDER_ID = "reminder_id";
    private static final String REMINDER_HEADER = "reminder_header";
    private static final String REMINDER_DESC = "reminder_desc";
    private static final String NOTIFICATION_WORK = "appName_notification_work";
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
    private String username = "";
    private String reminderId = "";
    private boolean isNewFlag = true;
    private DetailsViewModel viewModel;
    private MainNavigator nav;
    private SimpleDateFormat fullFormat;
    private DateFormat wordsFormat;
    private DateFormat dayFormat;

    public DetailsFragment() {
    }

    public static DetailsFragment getInstance(String id) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ID_KEY, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static DetailsFragment getInstance() {
        return new DetailsFragment();
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
        getUsername();
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
        if (isNewFlag) {menuItem.setTitle(R.string.add); }
        else {menuItem.setTitle(R.string.save);}
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

    public Reminder createReminderFromInput() {
        setUpdatedDetails();
        return new Reminder(reminderId, chosenReminderHeader, chosenReminderDescription, chosenTime);
    }

    public void setUpdatedDetails() {
        chosenReminderHeader = reminderHeaderET.getText().toString();
        chosenReminderDescription = reminderDescriptionET.getText().toString();
    }

    @SuppressLint("RestrictedApi")
    public void save() {
        if (isInputValid()) {
            if (isTimeValid()) {
                if (isNewFlag) {
                    setNewID();
                    viewModel.addReminder(createReminderFromInput(), username);
                    setNotify();
                    makeToast(getString(R.string.add_msg));
                } else {
                    viewModel.editReminder(createReminderFromInput(), username);
                    makeToast(getString(R.string.update_msg));
                }
                nav.toRemindersFragment();
            } else {
                makeToast(getString(R.string.select_valid_time_msg));
            }
        } else {
            makeToast(getString(R.string.enter_name_msg));
        }
    }

    private void setNotify() {
        if (chosenTime.getTimeInMillis() > currentTime.getTimeInMillis()) {
            setUpdatedDetails();
            Data data = new Data.Builder().putInt(NOTIFICATION_ID, 0)
                    .putString(REMINDER_HEADER, chosenReminderHeader)
                    .putString(REMINDER_DESC, chosenReminderDescription)
                    .build();
            long delay = chosenTime.getTimeInMillis() - currentTime.getTimeInMillis();
            scheduleNotification(delay, data);

            //String titleNotificationSchedule = getString(R.string.notification_schedule_title)
            //String patternNotificationSchedule = getString(R.string.notification_schedule_pattern)

            String titleNotificationSchedule = chosenReminderHeader;
            String patternNotificationSchedule = chosenReminderDescription;
//            make(
//                    coordinator_l,
//                    titleNotificationSchedule + SimpleDateFormat(
//                            patternNotificationSchedule, getDefault()
//                    ).format(chosenTime.time).toString(),
//                    LENGTH_LONG
//            ).show();
        } else {
            //val errorNotificationSchedule = getString(R.string.notification_schedule_error)
            String errorNotificationSchedule = "test3";
            //make(coordinator_l, errorNotificationSchedule, LENGTH_LONG).show();
        }
    }

    private void scheduleNotification(long delay, Data data) {
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWork.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build();

        WorkManager instanceWorkManager = WorkManager.getInstance(DetailsFragment.this.getContext());
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, notificationWork).enqueue();
    }

    public void setDetailsOnScreen() {
        todayDateNum = getDateString(currentTime);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isNewFlag = false;
            ((MainActivityInterface) getActivity()).changeToolbar(getString(R.string.edit_rem), true);
            reminderId = arguments.getString(ID_KEY, "");
            Reminder reminder = viewModel.getReminderByID(reminderId, username);
            chosenReminderHeader = reminder.getHeader();
            chosenReminderDescription = reminder.getDescription();
            chosenTime.set(reminder.getYear(), reminder.getMonth(), reminder.getDayOfMonth(),
                    reminder.getHour(), reminder.getMinutes());
            reminderHeaderET.setText(chosenReminderHeader);
            reminderDescriptionET.setText(chosenReminderDescription);
        } else {
            ((MainActivityInterface) getActivity()).changeToolbar(getString(R.string.add_rem), true);
            chosenTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH),
                    currentTime.get(Calendar.DATE), currentTime.get(Calendar.HOUR_OF_DAY),
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
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        nav = ((MainActivityInterface) getActivity()).getNavigator();
    }

    public void getUsername() {
        username = viewModel.getUsername();
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
        chosenDateNum = dayOfMonth + "/" + (month + 1) + "/" + year;
        Date date12 = null;
        try {
            date12 = fullFormat.parse(chosenDateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        chosenDateWords = wordsFormat.format(date12);
        dateTV.setText(chosenDateWords);
        chosenDayStr = dayFormat.format(date12);
        chosenTime.set(year, month + 1, dayOfMonth,
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