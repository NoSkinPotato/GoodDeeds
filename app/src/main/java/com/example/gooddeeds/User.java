package com.example.gooddeeds;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {

    public User(@NonNull String userID, @NonNull String email, String password, String firstName, String lastName, String phone, String address) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public User() {
    }

    public String userID;
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String phone;
    public String address;

}
