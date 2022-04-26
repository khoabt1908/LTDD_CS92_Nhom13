package com.example.todo.Model;

import java.util.ArrayList;

public class UserModel {
    private String id;
    private ArrayList<JobModel> jobList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public UserModel() {
        this.id = id;
        this.jobList = jobList;
    }



    public ArrayList<JobModel> getJobList() {
        return jobList;
    }

    public void setJobList(ArrayList<JobModel> jobList) {
        this.jobList = jobList;
    }
}
