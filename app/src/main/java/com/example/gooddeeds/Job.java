package com.example.gooddeeds;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "job_tbl",
        primaryKeys = {"id", "userGmail"})
public class Job {

    public Job(@NonNull String userGmail, String id, String title, String description, String address, int reward, String workerEmail) {
        this.userGmail = userGmail;
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.reward = reward;
        this.workerEmail = workerEmail;
    }

    @NonNull
    public String userGmail;
    @NonNull
    public String id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "reward")
    public int reward;
    @ColumnInfo(name = "worker")
    public String workerEmail;

}