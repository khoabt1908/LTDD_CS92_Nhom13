package com.example.todo;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter toDoAdapter;

    public RecyclerItemTouchHelper(ToDoAdapter toDoAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.toDoAdapter = toDoAdapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getBindingAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            //delete
            toDoAdapter.deleteItem(position);
        }
        else{
            //edit
            toDoAdapter.editItem(position);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            icon = ContextCompat.getDrawable(toDoAdapter.getContext(), R.drawable.ic_baseline_edit_24);
            background = new ColorDrawable(ContextCompat.getColor(toDoAdapter.getContext(), R.color.green));
        } else {
            icon = ContextCompat.getDrawable(toDoAdapter.getContext(), R.drawable.delete_white);
            background = new ColorDrawable(ContextCompat.getColor(toDoAdapter.getContext(), R.color.error));
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + iconMargin;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRigth = itemView.getLeft() + iconMargin + icon.getIntrinsicHeight();
            icon.setBounds(iconLeft, iconTop, iconRigth, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft()
                    + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        }else if(dX < 0){
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicHeight();
            int iconRigth = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRigth, iconBottom);

            background.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }else
            background.setBounds(0,0,0,0);
        background.draw(c);
        icon.draw(c);
    }
}
