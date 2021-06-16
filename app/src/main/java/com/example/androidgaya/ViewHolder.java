package com.example.androidgaya;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView header;
    TextView description;
    TextView time;
    TextView date;
    TextView day;
    Remainder remainder;
    RemainderAdapter.OnRemainderClicked onclick;

    ViewHolder(View itemView, RemainderAdapter.OnRemainderClicked onclick) {
        super(itemView);
        header = itemView.findViewById(R.id.rem_header_tv);
        description = itemView.findViewById(R.id.rem_description_tv);
        time = itemView.findViewById(R.id.rem_time_tv);
        date = itemView.findViewById(R.id.rem_date_tv);
        day = itemView.findViewById(R.id.rem_day_tv);
        this.onclick = onclick;
        itemView.setOnClickListener(this);
    }

    public void bind(Remainder reminder) {
        this.remainder = reminder;
        header.setText(reminder.getHeader());
        description.setText(reminder.getDescription());
        time.setText(reminder.getTime());
        date.setText(reminder.getDate());
        day.setText(reminder.getDay());
    }

    @Override
    public void onClick(View view) {
        onclick.click(remainder);
    }
}