package com.example.androidgaya.reminders.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidgaya.R;
import com.example.androidgaya.repositories.models.Reminder;
import java.util.List;

 public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {

    private final List<Reminder> reminders;
    private final OnReminderClicked onclick;
    private final OnReminderDelete onDelete;

    public ReminderAdapter(List<Reminder> reminders, OnReminderClicked onclick, OnReminderDelete onDelete) {
        this.reminders = reminders;
        this.onclick = onclick;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view,
                parent, false);
        return new ReminderViewHolder(view, onclick, onDelete);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
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
