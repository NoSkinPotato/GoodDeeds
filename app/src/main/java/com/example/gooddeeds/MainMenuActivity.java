package com.example.gooddeeds;

import static android.content.ContentValues.TAG;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private User currUser;

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

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        RecyclerView jobView = findViewById(R.id.JobView);
        jobView.setLayoutManager(new LinearLayoutManager(this));

        TextView noJobs = findViewById(R.id.NoJobsText);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        executor.execute(() -> {
            List<Job> jobs = db.jobDao().getAllJobs(currUser.email);

            mainHandler.post(() -> {
                if(!jobs.isEmpty()){
                    ItemAdapter adapter = new ItemAdapter(this, currUser, jobs);
                    jobView.setAdapter(adapter);
                }else{
                    jobView.setVisibility(View.GONE);
                    noJobs.setVisibility(View.VISIBLE);
                }
            });
        });

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
}