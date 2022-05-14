package com.example.todo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.ManageTaskFragment;
import com.example.todo.Model.TaskModel;
import com.example.todo.R;
import com.example.todo.SearchFragment;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<TaskModel> todoList;

    public SearchAdapter(SearchFragment activity) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ToDoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {
        TaskModel item = todoList.get(position);
        holder.task.setText(item.getTaskName());
        holder.task.setChecked(toBoolean(item.getStatus()));
    }

    public void setTasks(List<TaskModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    private Boolean toBoolean(int n){
        return n != 0;
    }
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }

}
