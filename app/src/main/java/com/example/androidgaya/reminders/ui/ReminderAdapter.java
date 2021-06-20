package com.example.androidgaya.reminders.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidgaya.R;
import com.example.androidgaya.repositories.Reminder;

import java.util.List;

 public class ReminderAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Reminder> reminders;
    private final OnReminderClicked onclick;

    ReminderAdapter(List<Reminder> reminders, OnReminderClicked onclick) {
        this.reminders = reminders;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view,
                parent, false);
        return new ViewHolder(view, onclick);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        if (reminders != null) {
            return reminders.size();
        }
        return 0;
    }

    public interface OnReminderClicked {
        void click(Reminder reminder);
    }
}
