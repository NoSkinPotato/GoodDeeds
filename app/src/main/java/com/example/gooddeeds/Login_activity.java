package com.example.gooddeeds;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login_activity extends AppCompatActivity {

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
        CheckBox remember = findViewById(R.id.rememberMeBox);
        TextView errMsg = findViewById(R.id.loginErrMsg);

        User rememberUser = RememberMe.getObject(this, "Remember");

        if(rememberUser != null){

            emailInput.setText(rememberUser.email);
            passInput.setText(rememberUser.password);

        }

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        JobClassDAO dao = db.jobDao();

        login.setOnClickListener(v -> {

            String email = emailInput.getText().toString();
            String pass = passInput.getText().toString();

            if(!email.isEmpty() && !pass.isEmpty()){

                User oldUser = dao.getUserbyEmail(email);
                if(oldUser != null){
                    if(pass.equals(oldUser.password)){

                        if(remember.isChecked()){
                            RememberMe.saveObject(this, "Remember", oldUser);
                        }

                        Intent intent = new Intent(this, MainMenuActivity.class);
                        intent.putExtra("User", oldUser);
                        startActivity(intent);

                    }else{
                        errMsg.setText("Invalid Password");
                    }
                }else{
                    errMsg.setText("Invalid Email");
                }
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
            startActivity(intent);
        });

    }
}