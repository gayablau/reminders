package com.example.androidgaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RemainderAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Remainder> remainders;
    private OnRemainderClicked onclick;

    RemainderAdapter(List<Remainder> remainders, OnRemainderClicked onclick) {
        this.remainders = remainders;
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
        Remainder remainder = remainders.get(position);
        holder.bind(remainder);
    }

    @Override
    public int getItemCount() {
        return remainders.size();
    }

    public interface OnRemainderClicked {
        void click(Remainder remainder);
    }
}
