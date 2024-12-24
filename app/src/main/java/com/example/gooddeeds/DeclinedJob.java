package com.example.gooddeeds;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class DeclinedJob {

    public DeclinedJob(@NonNull String userID, @NonNull String id) {
        this.userID = userID;
        this.id = id;
    }

    public DeclinedJob(){};

    @NonNull
    public String userID;
    @NonNull
    public String id;

}
