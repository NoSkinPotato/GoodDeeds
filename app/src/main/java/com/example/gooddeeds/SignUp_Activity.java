package com.example.gooddeeds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class SignUp_Activity extends AppCompatActivity {

    private static final String TAG = "SignIn";
    private TextView errMsg;
    private String fname;
    private String em;
    private String phn;
    private String add;
    private String pass;
    private String lname;


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

        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText phone = findViewById(R.id.phone);
        EditText address = findViewById(R.id.address);
        EditText createPass = findViewById(R.id.createPass);
        EditText confirmPass = findViewById(R.id.confirmPass);
        Button signUpBtn = findViewById(R.id.SignUpBtn);
        errMsg = findViewById(R.id.signErrMsg);

        signUpBtn.setOnClickListener(func -> {

            fname = firstName.getText().toString();
            em = email.getText().toString();
            phn = phone.getText().toString();
            add = address.getText().toString();
            pass = createPass.getText().toString();
            lname = lastName.getText().toString();

            if (!fname.isEmpty() &&
                    !em.isEmpty() &&
                    !phn.isEmpty() &&
                    !add.isEmpty() &&
                    !pass.isEmpty()) {


                if (pass.equals(confirmPass.getText().toString())) {

                    CheckUserEmail(this, em);

                } else {
                    errMsg.setText("Password confirmation is incorrect");
                }
            } else {
                errMsg.setText("All Data Must Be Filled");
            }
        });

        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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

    private void CheckUserEmail(Context context, String email) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && user.email.equals(email)) {
                        errMsg.setText("The Following Email already exist");
                        return;
                    }
                }

                GenerateUserAccount(context, Post_Job_Activity.getRandomString(3));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void GenerateUserAccount(Context context, String userID) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    GenerateUserAccount(context, Post_Job_Activity.getRandomString(3));
                    return;
                }
                User newUser = new User(userID, em, pass, fname, lname, phn, add);
                myRef.child(userID).setValue(newUser)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "User added successfully.");
                            Intent intent = new Intent(context, MainMenuActivity.class);
                            intent.putExtra("User", newUser);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding user: " + e.getMessage());
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}