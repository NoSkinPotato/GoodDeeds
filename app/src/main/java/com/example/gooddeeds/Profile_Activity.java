package com.example.gooddeeds;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Activity extends AppCompatActivity {

    private User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currUser = (User) getIntent().getSerializableExtra("User");


        TextView fullName = findViewById(R.id.fullNameText);
        TextView firstName = findViewById(R.id.firstNameText);
        TextView lastName = findViewById(R.id.lastNameText);
        TextView phone = findViewById(R.id.phoneText);
        TextView address = findViewById(R.id.addressText);
        TextView email = findViewById(R.id.emailText);

        String name = currUser.firstName + " " + currUser.lastName;
        fullName.setText(name);
        firstName.setText(currUser.firstName);
        lastName.setText(currUser.lastName);
        phone.setText(currUser.phone);
        address.setText(currUser.address);
        email.setText(currUser.email);



    }
}