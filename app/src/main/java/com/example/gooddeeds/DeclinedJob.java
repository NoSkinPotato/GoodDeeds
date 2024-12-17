package com.example.gooddeeds;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "declined_job_tbl",
        primaryKeys = {"posterGmail", "userGmail", "id"})
public class DeclinedJob {

    public DeclinedJob(@NonNull String userGmail, @NonNull String posterGmail, int id) {
        this.userGmail = userGmail;
        this.posterGmail = posterGmail;
        this.id = id;
    }

    @NonNull
    public String userGmail;
    @NonNull
    public String posterGmail;

    public int id;

}
