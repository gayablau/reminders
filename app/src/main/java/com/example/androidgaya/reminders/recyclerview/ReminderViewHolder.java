package com.example.androidgaya.reminders.recyclerview;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidgaya.R;
import com.example.androidgaya.repositories.models.Reminder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView header_tv;
    TextView description_tv;
    TextView time_tv;
    TextView date_tv;
    TextView day_tv;
    Reminder reminder;

    ReminderAdapter.OnReminderClicked onclick;
    OnReminderDelete onDelete;

    ReminderViewHolder(View itemView, ReminderAdapter.OnReminderClicked onclick, OnReminderDelete onDelete) {
        super(itemView);
        header_tv = itemView.findViewById(R.id.rem_header_tv);
        description_tv = itemView.findViewById(R.id.rem_description_tv);
        time_tv = itemView.findViewById(R.id.rem_time_tv);
        date_tv = itemView.findViewById(R.id.rem_date_tv);
        day_tv = itemView.findViewById(R.id.rem_day_tv);
        this.onclick = onclick;
        this.onDelete = onDelete;
        itemView.setOnClickListener(this);
    }

    public void bind(Reminder reminder) {
        this.reminder = reminder;
        Calendar calendar = reminder.getCalendar();
        setTimeText(calendar);
        header_tv.setText(reminder.getHeader());
        description_tv.setText(reminder.getDescription());

    }

    public void setTimeText(Calendar calendar) {
        SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        DateFormat dayFormat = new SimpleDateFormat("EEEE", java.util.Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        Date date = calendar.getTime();
        date.setMonth(calendar.get(Calendar.MONTH) - 1);
        time_tv.setText(timeFormat.format(date));
        date_tv.setText(fullFormat.format(date));
        day_tv.setText(dayFormat.format(date));
    }

    @Override
    public void onClick(View view) {
        onclick.click(reminder);
    }

    public void onSwipe() {
        onDelete.delete(reminder);
    }
}