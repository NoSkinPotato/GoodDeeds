package com.example.gooddeeds;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "declined_job_tbl",
        primaryKeys = {"userGmail", "id"})
public class DeclinedJob {

    public DeclinedJob(@NonNull String userGmail, @NonNull String id) {
        this.userGmail = userGmail;
        this.id = id;
    }

    @NonNull
    public String userGmail;
    @NonNull
    public String id;

}
