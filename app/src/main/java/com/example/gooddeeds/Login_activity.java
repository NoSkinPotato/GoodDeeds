package com.example.gooddeeds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_activity extends AppCompatActivity {

    private static final String TAG = "Login";

    private CheckBox remember;
    private TextView errMsg;

    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = findViewById(R.id.loginButton2);
        EditText emailInput = findViewById(R.id.LoginEmailInput);
        EditText passInput = findViewById(R.id.LoginPassInput);
        ImageView seePass = findViewById(R.id.seePassLogin);
        ImageView backBtn = findViewById(R.id.BackBtn);
        remember = findViewById(R.id.rememberMeBox);
        errMsg = findViewById(R.id.loginErrMsg);

        User rememberUser = RememberMe.getObject(this, "Remember");

        if(rememberUser != null){
            Log.d("Saved", "isLoaded");
            emailInput.setText(rememberUser.email);
            passInput.setText(rememberUser.password);
        }

        login.setOnClickListener(v -> {

            String email = emailInput.getText().toString();
            pass = passInput.getText().toString();

            if(!email.isEmpty() && !pass.isEmpty()){
                GenerateUserLogin(email, pass,this);

            }else{
                errMsg.setText("Both Password and Email must be filled");
            }
        });



        seePass.setOnClickListener(func -> {
            if (passInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {

                passInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {

                passInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            passInput.setSelection(passInput.getText().length());
        });


        backBtn.setOnClickListener(func -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }

    private void GenerateUserLogin(String email, String password, Context context){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnapShot : snapshot.getChildren()){
                    User user = userSnapShot.getValue(User.class);
                    if (user != null && user.email.equals(email)){
                        if (user.password.equals(password)){
                            if(remember.isChecked()){
                                Log.d("Saved", "isSaved");
                                RememberMe.saveObject(context, "Remember", user);
                            }
                            Intent intent = new Intent(context, MainMenuActivity.class);
                            intent.putExtra("User", user);
                            context.startActivity(intent);
                        }else{
                            errMsg.setText("Wrong Password");
                        }
                        return;
                    }
                }
                errMsg.setText("Email not found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });
    }
}