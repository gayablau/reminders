package com.example.androidgaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.MyViewHolder> {

    private final List<Remainder> remainders;
    private final LayoutInflater mInflater;
    private OnRemainderClicked onclick;

    RemainderAdapter(Context context, List<Remainder> remainders, OnRemainderClicked onclick) {
        this.mInflater = LayoutInflater.from(context);
        this.remainders = remainders;
        this.onclick = onclick;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new MyViewHolder(view, onclick);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Remainder remainder = remainders.get(position);
        holder.bind(remainder);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return remainders.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView header;
        TextView description;
        TextView time;
        TextView date;
        TextView day;
        Remainder remainder;
        OnRemainderClicked onclick;

        MyViewHolder(View itemView, OnRemainderClicked onclick) {
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
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            onclick.click(remainder);
        }
    }

    // convenience method for getting data at click position
    Remainder getItem(int id) {
        return remainders.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnRemainderClicked {
        void click(Remainder remainder);
    }
}
