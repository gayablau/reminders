package com.example.androidgaya;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final RemainderAdapter remainderAdapter;
    private final ColorDrawable background;
    private final String username;

    public SwipeToDeleteCallback(RemainderAdapter adapter, String username) {
        super(0, ItemTouchHelper.LEFT);
        this.remainderAdapter = adapter;
        this.username = username;
        // Set background color for swipe
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        // delete the chosen remainder
        if (RemaindersBase.get().deleteRemainder(viewHolder.getAdapterPosition(), username)) {
            remainderAdapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        // Set background for swipe
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        if (dX < 0) { // Swiping to the left
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
    }
}