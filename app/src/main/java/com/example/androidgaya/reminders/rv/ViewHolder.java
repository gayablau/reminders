package com.example.androidgaya.reminders.rv;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidgaya.R;
import com.example.androidgaya.repositories.Reminder;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView header;
    TextView description;
    TextView time;
    TextView date;
    TextView day;
    Reminder reminder;
    ReminderAdapter.OnReminderClicked onclick;

    ViewHolder(View itemView, ReminderAdapter.OnReminderClicked onclick) {
        super(itemView);
        header = itemView.findViewById(R.id.rem_header_tv);
        description = itemView.findViewById(R.id.rem_description_tv);
        time = itemView.findViewById(R.id.rem_time_tv);
        date = itemView.findViewById(R.id.rem_date_tv);
        day = itemView.findViewById(R.id.rem_day_tv);
        this.onclick = onclick;
        itemView.setOnClickListener(this);
    }

    public void bind(Reminder reminder) {
        this.reminder = reminder;
        header.setText(reminder.getHeader());
        description.setText(reminder.getDescription());
        time.setText(reminder.getTime());
        date.setText(reminder.getDate());
        day.setText(reminder.getDay());
    }

    @Override
    public void onClick(View view) {
        onclick.click(reminder);
    }
}