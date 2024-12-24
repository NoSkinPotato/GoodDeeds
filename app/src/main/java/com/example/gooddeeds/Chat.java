package com.example.gooddeeds;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Chat implements Serializable {

    public Chat(@NonNull String chatID, int textID, @NonNull String senderID, String text) {
        this.chatID = chatID;
        this.textID = textID;
        this.senderID = senderID;
        this.text = text;
    }

    public Chat(){

    }

    @NonNull
    public String chatID;
    @NonNull
    public int textID;
    @NonNull
    public String senderID;
    public String text;

}