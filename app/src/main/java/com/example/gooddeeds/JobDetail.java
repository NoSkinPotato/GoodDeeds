package com.example.gooddeeds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JobDetail extends AppCompatActivity {

    private final static String TAG = "JobDetail";
    private boolean yourJob;
    private User currUser;
    private Job currJob;
    private TextView posterText;
    private Button removeBtn;
    private Button acceptBtn;
    private Button declineBtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_detail2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currUser = (User) getIntent().getSerializableExtra("User");
        currJob = (Job) getIntent().getSerializableExtra("Job");


        ImageView backBtn = findViewById(R.id.ButtonToGoBack);

        backBtn.setOnClickListener(func -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("User", currUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        posterText = findViewById(R.id.posterText);

        if (Objects.equals(currJob.userID, currUser.userID)){
            yourJob = true;
        }

        TextView jobTitle = findViewById(R.id.jobTitle);
        TextView jobLocation = findViewById(R.id.jobLocation);
        TextView jobDescr = findViewById(R.id.jobDescription);
        TextView jobReward = findViewById(R.id.rewardText);

        if(currJob.reward == 0){
            jobReward.setText("Volunteer Work");
        }else{
            String reward = "Rp " + ItemAdapter.formatNumberWithDots(currJob.reward);
            jobReward.setText(reward);
        }

        jobTitle.setText(currJob.title.isEmpty() ? " " : currJob.title);
        jobLocation.setText(currJob.address.isEmpty() ? " " : currJob.address);
        jobDescr.setText(currJob.description.isEmpty() ? " " : currJob.description);

        TextView awaitWorkers = findViewById(R.id.waitWorkers);
        removeBtn = findViewById(R.id.removeJobBtn);
        acceptBtn = findViewById(R.id.AcceptButton);
        declineBtn = findViewById(R.id.declineButton);

        if (yourJob) {
            acceptBtn.setVisibility(View.GONE);
            declineBtn.setVisibility(View.GONE);

        }else{
            awaitWorkers.setVisibility(View.GONE);
            removeBtn.setVisibility(View.GONE);
        }

        ViewJobDetail(this);

    }


    private void ViewJobDetail(Context context){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child(currJob.userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User posterUser = snapshot.getValue(User.class);
                String x = yourJob ? "You" : posterUser.firstName + " " + posterUser.lastName;
                posterText.setText("Posted by: " + x);

                DatabaseReference jobRef = database.getReference("Job");

                removeBtn.setOnClickListener(func -> {

                    jobRef.child(currJob.id).removeValue()
                            .addOnSuccessListener(e -> {
                                    Log.d(TAG, "Job Deleted Successfully");
                            })
                            .addOnFailureListener(e -> {
                                    Log.d(TAG, "Job Failed To Delete");
                            });

                    Intent intent = new Intent(context, MainMenuActivity.class);
                    intent.putExtra("User", currUser);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                });

                DatabaseReference declineJobRef = database.getReference("DeclineJob");

                declineBtn.setOnClickListener(func -> {

                    DeclinedJob dj = new DeclinedJob(currUser.userID, currJob.id);
                    String declineID = dj.userID + "|" + dj.id;
                    declineJobRef.child(declineID).setValue(dj)
                            .addOnSuccessListener(e -> {
                                Log.d(TAG, "Job Declined Successfully");
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "Failed To Decline Job");
                            });

                    Intent intent = new Intent(context, MainMenuActivity.class);
                    intent.putExtra("User", currUser);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                });

                acceptBtn.setOnClickListener(func -> {

                    Map<String, Object> update = new HashMap<>();
                    update.put("workerID", currUser.userID);

                    jobRef.child(currJob.id).updateChildren(update)
                            .addOnSuccessListener(e -> {
                                Log.d(TAG, "Worker ID Updated");
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "Fail To Update Worker ID");
                            });

                    Intent intent = new Intent(context, ChatMenuActivity.class);
                    intent.putExtra("User", currUser);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });
    }

}