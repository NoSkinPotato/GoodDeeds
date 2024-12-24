package com.example.gooddeeds;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Job implements Serializable {

    public Job(@NonNull String userID, @NonNull String id, String title, String description, String address, int reward, String workerID) {
        this.userID = userID;
        this.title = title;
        this.id = id;
        this.description = description;
        this.address = address;
        this.reward = reward;
        this.workerID = workerID;
    }

    public Job(){

    }

    public String userID;
    public String id;
    public String title;
    public String description;
    public String address;
    public int reward;
    public String workerID;

}