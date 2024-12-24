package com.example.gooddeeds;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private User currUser;

    private List<Job> allJobs = new ArrayList<>();
    TextView noJobs;
    RecyclerView jobView;

    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainmenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currUser = (User) getIntent().getSerializableExtra("User");

        jobView = findViewById(R.id.JobView);
        jobView.setLayoutManager(new LinearLayoutManager(this));

        noJobs = findViewById(R.id.NoJobsText);

        GenerateJobs(this);

        Button addJobBtn = findViewById(R.id.AddJobButton);
        addJobBtn.setOnClickListener(e -> {

            Intent intent = new Intent(this, Post_Job_Activity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });

        LinearLayout profBtn = findViewById(R.id.profileButton);

        profBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, Profile_Activity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });

        LinearLayout chatBtn = findViewById(R.id.chatButton);

        chatBtn.setOnClickListener(e -> {
            Intent intent  = new Intent(this, ChatMenuActivity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });

    }

    private void GenerateJobs(Context context) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Job");
        DatabaseReference decRef = database.getReference("DeclineJob");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (allJobs != null) allJobs.clear();
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null && job.workerID == null) {
                        allJobs.add(job);
                    }
                }

                decRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (Job job : allJobs){
                            String x = currUser.userID + "|" + job.id;
                            for (DataSnapshot data : snapshot.getChildren()){
                                Log.d(TAG, data.getKey() + " | " + x);
                                if (Objects.equals(data.getKey(), x)){
                                    Log.d(TAG, "Remove job : " + job.id);
                                    allJobs.remove(job);
                                }
                            }
                        }

                        if(!allJobs.isEmpty()){
                            ItemAdapter adapter = new ItemAdapter(context, currUser, allJobs);
                            jobView.setAdapter(adapter);
                        }else{
                            Log.d(TAG, "empty");
                            jobView.setVisibility(View.GONE);
                            noJobs.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });


    }

}

