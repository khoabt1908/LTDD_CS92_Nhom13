package com.example.todo.Model;

import java.util.ArrayList;

public class UserModel {
    private int id;
    private ArrayList<JobModel> jobList;

    public UserModel() {
        this.id = id;
        this.jobList = jobList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<JobModel> getJobList() {
        return jobList;
    }

    public void setJobList(ArrayList<JobModel> jobList) {
        this.jobList = jobList;
    }
}
