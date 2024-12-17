package com.example.gooddeeds;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_tbl",
        primaryKeys = {"senderEmail", "receiverEmail", "textID"})
public class Chat {

    public Chat (@NonNull String senderEmail, @NonNull String receiverEmail, @NonNull String textID, @NonNull String text){

        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.textID = textID;
        this.text = text;
    }
    @PrimaryKey
    public String senderEmail;
    @ColumnInfo(name = "receiverEmail")
    public String receiverEmail;
    @ColumnInfo(name = "textID")
    public String textID;
    @ColumnInfo(name = "text")
    public String text;
}