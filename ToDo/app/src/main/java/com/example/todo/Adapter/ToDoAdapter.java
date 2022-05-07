package com.example.todo.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.ManageTaskFragment;
import com.example.todo.Model.TaskModel;
import com.example.todo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<TaskModel> todoList;
    private ManageTaskFragment activity;
    private Context context;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

    public Context getContext() {
        return context;
    }

    public ToDoAdapter(ManageTaskFragment activity) {
        this.todoList = todoList;
        this.activity =activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskModel item = todoList.get(position);
        item.setId(position);
        holder.task.setText(item.getTaskName());
        holder.task.setChecked(toBoolean(item.getStatus()));

        if (item.getStatus() == 1)
            holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.task.setPaintFlags(holder.task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));


        holder.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("check date " + item.getEndDate());
                if (holder.task.isChecked()) {
                    holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.task.setPaintFlags(holder.task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

            }
        });
    }

    public void setTasks(List<TaskModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return todoList.size();
    }

    private Boolean toBoolean(int n) {
        return n != 0;
    }

    public void editItem(int pos) {
        System.out.println(pos + "edit");
    }

    public void deleteItem(int pos) {
        TaskModel item = todoList.get(pos);
        item.setIsDelete(1);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.child(user.getUid()).child("jobList")
                .child(String.valueOf(activity.getCurrentJob()))
                .child("taskList").child(String.valueOf(item.getId()))
                .setValue(item);

//        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).child("jobList")
//                .child(String.valueOf(activity.getCurrentJob()))
//                .child("taskList").child(String.valueOf(pos))
//                .get();
//        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//
//            }
//        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
