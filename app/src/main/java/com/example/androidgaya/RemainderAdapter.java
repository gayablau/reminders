package com.example.androidgaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ViewHolder> {

    private List<Remainder> remainders;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private static Context context;

    RemainderAdapter(Context context, List<Remainder> remainders) {
        this.mInflater = LayoutInflater.from(context);
        this.remainders = remainders;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_view, parent, false);
        context = parent.getContext();
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
            header = (TextView) itemView.findViewById(R.id.remHeader);
            description = (TextView) itemView.findViewById(R.id.remDescription);
            time = (TextView) itemView.findViewById(R.id.remTime);
            date = (TextView) itemView.findViewById(R.id.remDate);
            day = (TextView) itemView.findViewById(R.id.remDay);

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

    public static Context getContext() {
        return context;
    }
}
