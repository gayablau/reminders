package com.example.androidgaya.reminders.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidgaya.R;
import com.example.androidgaya.repositories.models.ReminderEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView header_tv;
    TextView description_tv;
    TextView time_tv;
    TextView date_tv;
    TextView day_tv;
    ReminderEntity reminderEntity;
    ReminderAdapter.OnReminderClicked onclick;
    OnReminderDelete onDelete;

    ReminderViewHolder(View itemView,
                       ReminderAdapter.OnReminderClicked onclick,
                       OnReminderDelete onDelete) {
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

    public void bind(ReminderEntity reminderEntity) {
        this.reminderEntity = reminderEntity;
        Calendar calendar = reminderEntity.getCalendar();
        setTimeText(calendar);
        header_tv.setText(reminderEntity.getHeader());
        description_tv.setText(reminderEntity.getDescription());
    }

    public void setTimeText(Calendar calendar) {
        SimpleDateFormat fullFormat = new SimpleDateFormat(itemView.getResources().getString(R.string.full_date),
                java.util.Locale.getDefault());
        DateFormat dayFormat = new SimpleDateFormat(itemView.getResources().getString(R.string.day_date),
                java.util.Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat(itemView.getResources().getString(R.string.hour),
                java.util.Locale.getDefault());
        Date date = calendar.getTime();
        Calendar updatedCal = Calendar.getInstance();
        updatedCal.setTime(date);
        //updatedCal.add(Calendar.MONTH, -1);
        //Todo - fix
        //updatedCal.add(Calendar.DATE, 1);
        date = updatedCal.getTime();
        time_tv.setText(timeFormat.format(date));
        date_tv.setText(fullFormat.format(date));
        day_tv.setText(dayFormat.format(date));
    }

    @Override
    public void onClick(View view) {
        onclick.click(reminderEntity);
    }

    public void onSwipe() {
        onDelete.delete(reminderEntity);
    }
}