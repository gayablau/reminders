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
    private ItemClickListener mClickListener;

    RemainderAdapter(Context context, List<Remainder> remainders) {
        this.mInflater = LayoutInflater.from(context);
        this.remainders = remainders;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remainder remainder = remainders.get(position);
        holder.header.setText(remainder.getHeader());
        holder.description.setText(remainder.getDescription());
        holder.time.setText(remainder.getTime());
        holder.date.setText(remainder.getDate());
        holder.day.setText(remainder.getDay());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return remainders.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView header;
        TextView description;
        TextView time;
        TextView date;
        TextView day;

        ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.remHeader);
            description = itemView.findViewById(R.id.rem_description);
            time = itemView.findViewById(R.id.rem_time);
            date = itemView.findViewById(R.id.rem_date);
            day = itemView.findViewById(R.id.rem_day);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Remainder getItem(int id) {
        return remainders.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
