package com.example.androidgaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ViewHolder> {

    private final List<Remainder> remainders;
    private final LayoutInflater mInflater;
    private OnRemainderClicked onclick;

    RemainderAdapter(Context context, List<Remainder> remainders, OnRemainderClicked onclick) {
        this.mInflater = LayoutInflater.from(context);
        this.remainders = remainders;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(view, onclick);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remainder remainder = remainders.get(position);
        holder.bind(remainder);
    }

    @Override
    public int getItemCount() {
        return remainders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView header;
        TextView description;
        TextView time;
        TextView date;
        TextView day;
        Remainder remainder;
        OnRemainderClicked onclick;

        ViewHolder(View itemView, OnRemainderClicked onclick) {
            super(itemView);
            header = itemView.findViewById(R.id.remHeader);
            description = itemView.findViewById(R.id.rem_description);
            time = itemView.findViewById(R.id.rem_time);
            date = itemView.findViewById(R.id.rem_date);
            day = itemView.findViewById(R.id.rem_day);
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

    Remainder getItem(int id) {
        return remainders.get(id);
    }

    public interface OnRemainderClicked {
        void click(Remainder remainder);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
