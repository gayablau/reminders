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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remainder remainder = remainders.get(position);
        holder.header.setText(remainder.getHeader());
        holder.description.setText(remainder.getDescription());
        holder.time.setText(remainder.getTime());
        holder.date.setText(remainder.getDate());
        holder.day.setText(remainder.getDay());
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

    Remainder getItem(int id) {
        return remainders.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
