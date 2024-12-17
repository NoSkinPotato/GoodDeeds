package com.example.gooddeeds;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        JobClassDAO dao = db.jobDao();

        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText phone = findViewById(R.id.phone);
        EditText address = findViewById(R.id.address);
        EditText createPass = findViewById(R.id.createPass);
        EditText confirmPass = findViewById(R.id.confirmPass);
        Button signUpBtn = findViewById(R.id.SignUpBtn);
        TextView errMsg = findViewById(R.id.signErrMsg);

        signUpBtn.setOnClickListener(func -> {

            String fname = firstName.getText().toString();
            String em = email.getText().toString();
            String phn = phone.getText().toString();
            String add = address.getText().toString();
            String pass = createPass.getText().toString();

            if (!fname.isEmpty() &&
                !em.isEmpty() &&
                !phn.isEmpty() &&
                !add.isEmpty() &&
                !pass.isEmpty()){


                if(pass.equals(confirmPass.getText().toString())){

                    if(dao.CheckEmail(em) != null){
                        errMsg.setText("This email already exist");
                        return;
                    }


                    User newUser = new User(em, pass, fname, lastName.getText().toString(), phn, add);
                    dao.insertUser(newUser);

                    Intent intent = new Intent(this, MainMenuActivity.class);
                    intent.putExtra("User", newUser);
                    startActivity(intent);

                }else{
                    errMsg.setText("Password confirmation is incorrect");
                }
            }else{
                errMsg.setText("All Data Must Be Filled");
            }
        });


        ImageView createPassEye = findViewById(R.id.passCreateSign);
        ImageView confirmPassEye = findViewById(R.id.passConfirmSign);

        createPassEye.setOnClickListener(func -> {
            if (createPass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {

                createPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {

                createPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            createPass.setSelection(createPass.getText().length());
        });

        confirmPassEye.setOnClickListener(func -> {
            if (confirmPass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {

                confirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {

                confirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            confirmPass.setSelection(confirmPass.getText().length());
        });


    }
}