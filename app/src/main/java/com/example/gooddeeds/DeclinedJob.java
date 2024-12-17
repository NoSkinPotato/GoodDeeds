package com.example.gooddeeds;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "declined_job_tbl",
        primaryKeys = {"userGmail", "posterGmail", "id"})
public class DeclinedJob {

    public DeclinedJob(@NonNull String userGmail, @NonNull String posterGmail, @NonNull String id) {
        this.userGmail = userGmail;
        this.posterGmail = posterGmail;
        this.id = id;
    }

    @NonNull
    public String userGmail;
    @NonNull
    public String posterGmail;
    @NonNull
    public String id;

}
