package com.example.androidgaya.reminders.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidgaya.R;
import com.example.androidgaya.repositories.models.ReminderEntity;

import java.util.ArrayList;
import java.util.List;

 public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {

    private final MutableLiveData<ArrayList<ReminderEntity>> reminderEntities;
    private final OnReminderClicked onclick;
    private final OnReminderDelete onDelete;

    public ReminderAdapter(MutableLiveData<ArrayList<ReminderEntity>> reminderEntities, OnReminderClicked onclick, OnReminderDelete onDelete) {
        this.reminderEntities = reminderEntities;
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
        ReminderEntity reminderEntity = reminderEntities.getValue().get(position);
        holder.bind(reminderEntity);
    }

    @Override
    public int getItemCount() {
        try {return reminderEntities.getValue().size();} catch (Exception ex) {return 0;}
    }

    public interface OnReminderClicked {
        void click(ReminderEntity reminderEntity);
    }
}
