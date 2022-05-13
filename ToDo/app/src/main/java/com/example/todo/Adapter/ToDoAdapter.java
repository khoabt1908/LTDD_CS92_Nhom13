package com.example.todo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.FirstLogin;
import com.example.todo.Login;
import com.example.todo.Main;
import com.example.todo.ManageTaskFragment;
import com.example.todo.Model.TaskModel;
import com.example.todo.Model.UserModel;
import com.example.todo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.thanguit.toastperfect.ToastPerfect;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<TaskModel> todoList;
    private ManageTaskFragment activity;
    private Context context;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

    public ToDoAdapter(ManageTaskFragment activity) {
        this.todoList = todoList;
        this.activity = activity;
    }

    public Context getContext() {
        return context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskModel item = todoList.get(position);
        holder.task.setText(item.getTaskName());
        holder.task.setChecked(toBoolean(item.getStatus()));

        if (item.getStatus() == 1)
            holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.task.setPaintFlags(holder.task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        if (activity.getIsDelete() == 1) {
            holder.task.setClickable(false);
        }

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println(b);
                changeTaskStatus(item.getId(), b);
                if (b) {
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

    public void changeTaskStatus(String id, boolean newStatus) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                for (int i = 0; i < userModel.getJobList()
                        .get(activity.getCurrentJob()).getTaskList().size(); i++) {
                    TaskModel taskModel = userModel.getJobList()
                            .get(activity.getCurrentJob()).getTaskList().get(i);
                    if (taskModel.getId().equals(id)) {
                        if (newStatus) {
                            Date date = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            String strDate;
                            strDate = formatter.format(date);
                            userModel.getJobList().get(activity.getCurrentJob()).getTaskList().get(i).setEndDate(strDate);
                        } else {
                            userModel.getJobList().get(activity.getCurrentJob()).getTaskList().get(i).setEndDate("");
                        }
                        userModel.getJobList()
                                .get(activity.getCurrentJob()).getTaskList().get(i).setStatus(newStatus ? 1 : 0);
                    }
                }
                mDatabase.child(user.getUid()).setValue(userModel);
            }
        });
    }

    public int getItemCount() {
        return todoList.size();
    }

    private Boolean toBoolean(int n) {
        return n != 0;
    }

    public void cfDeleteItem(int pos){
        TaskModel item = todoList.get(pos);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                List<TaskModel> taskList = new ArrayList<>();
                for (int i = 0; i < userModel.getJobList().size(); i++) {
                    taskList = userModel.getJobList().get(i).getTaskList();
                    if (taskList != null && taskList.size() > 0) {
                        for (int j=0;j<taskList.size();j++)
                        {
                            TaskModel taskItem = taskList.get(j);
                            if (taskItem.getId().equals(item.getId())) {
                                userModel.getJobList().get(i).getTaskList().remove(j);
                                mDatabase.child(user.getUid()).setValue(userModel);
                                ToastPerfect.makeText(getContext(), ToastPerfect.SUCCESS, "Xoá hoàn tất"
                                        , ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    public void recoveyItem(int pos){
        TaskModel item = todoList.get(pos);
        item.setIsDelete(0);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                List<TaskModel> taskList = new ArrayList<>();
                for (int i = 0; i < userModel.getJobList().size(); i++) {
                    taskList = userModel.getJobList().get(i).getTaskList();
                    if (taskList != null && taskList.size() > 0) {
                        for (int j=0;j<taskList.size();j++)
                        {
                            TaskModel taskItem = taskList.get(j);
                            if (taskItem.getId().equals(item.getId())) {

                                userModel.getJobList().get(i).getTaskList().get(j).setIsDelete(0);
                                mDatabase.child(user.getUid()).setValue(userModel);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    public void editItem(int pos) {
        TaskModel item = todoList.get(pos);
        activity.loadEditFragment(item.getId(), activity.getCurrentJob());
    }

    public void deleteItem(int pos) {
        TaskModel item = todoList.get(pos);
        item.setIsDelete(1);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                for (int i = 0; i < userModel.getJobList().get(activity.getCurrentJob()).getTaskList().size(); i++) {
                    TaskModel taskItem = userModel.getJobList().get(activity.getCurrentJob()).getTaskList().get(i);
                    if (taskItem.getId().equals(item.getId())) {
                        DatabaseReference currentItem = mDatabase.child(user.getUid()).child("jobList")
                                .child(String.valueOf(activity.getCurrentJob())).child("taskList")
                                .child(String.valueOf(i));
                        currentItem.setValue(item);
                        userModel.getJobList().get(activity.getCurrentJob()).getTaskList().get(i).setIsDelete(1);
                        activity.loadData(activity.getCurrentJob(), activity.getIsDelete());
                        break;
                    }
                }
                mDatabase.child(user.getUid()).setValue(userModel);
            }
        });
        ToastPerfect.makeText(getContext(), ToastPerfect.SUCCESS, "Đã chuyển công việc vào thùng rác", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
