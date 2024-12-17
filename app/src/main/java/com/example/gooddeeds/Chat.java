package com.example.gooddeeds;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "chat_tbl",
        primaryKeys = {"chatID", "textID"})
public class Chat implements Serializable {

    public Chat(@NonNull String chatID, int textID, @NonNull String text) {
        this.chatID = chatID;
        this.textID = textID;
        this.text = text;
    }

    @NonNull
    public String chatID;
    @ColumnInfo(name = "textID")
    public int textID;
    @NonNull
    @ColumnInfo(name = "text")
    public String text;
}