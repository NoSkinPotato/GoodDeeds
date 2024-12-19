package com.example.gooddeeds;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

public class Profile_Activity extends AppCompatActivity {

    private User currUser;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        }
    }

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

        Button logoutBtn = findViewById(R.id.logOutBtn);

        logoutBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        LinearLayout homeBtn = findViewById(R.id.homeButton);

        homeBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });

        LinearLayout chatBtn = findViewById(R.id.chatButton);

        chatBtn.setOnClickListener(e -> {
            Intent intent  = new Intent(this, ChatMenuActivity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });


        Button changePhoto = findViewById(R.id.changePhoto);

        changePhoto.setOnClickListener(e -> {
            openGallery();
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

}