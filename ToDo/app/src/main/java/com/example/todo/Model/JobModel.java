package com.example.todo.Model;

import java.util.ArrayList;

public class JobModel {
    private String id;
    private String name;
    private String color;
    private String startDate;
    private ArrayList<TaskModel> taskList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public ArrayList<TaskModel> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<TaskModel> taskList) {
        this.taskList = taskList;
    }

    public JobModel() {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startDate = startDate;
        this.taskList = taskList;
    }
}
