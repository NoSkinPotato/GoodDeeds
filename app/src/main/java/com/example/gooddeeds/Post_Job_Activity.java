package com.example.gooddeeds;

import static android.content.ContentValues.TAG;

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

import java.util.Random;

public class Post_Job_Activity extends AppCompatActivity {

    private User currUserPost;
    private TextView title;
    private TextView reward;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        currUserPost = (User) intent.getSerializableExtra("User");

        title = findViewById(R.id.jobTitleInput);
        reward = findViewById(R.id.jobRewardInput);
        description = findViewById(R.id.jobDescInput);

        Button postJob = findViewById(R.id.PostJob);

        postJob.setOnClickListener(func -> {

            AddJob(this, getRandomString(3));

        });

        ImageView backHome = findViewById(R.id.BackHome);

        backHome.setOnClickListener(func -> {
            Intent intent1 = new Intent(this, MainMenuActivity.class);
            intent1.putExtra("User", currUserPost);
            startActivity(intent1);
        });


    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void AddJob(Context context, String jobID) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Job");

        myRef.child(jobID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String newId = getRandomString(3);
                    AddJob(context, newId);
                    return;
                }

                int rew;
                if(reward.getText().toString().isEmpty())
                    rew = 0;
                else
                    rew = Integer.parseInt(reward.getText().toString());

                Job newJob = new Job(currUserPost.userID, jobID, title.getText().toString(), description.getText().toString(), currUserPost.address, rew, null);

                myRef.child(jobID).setValue(newJob)
                        .addOnSuccessListener(aVoid -> Log.d("Database", "Job added successfully"))
                        .addOnFailureListener(e -> Log.e("Database", "Error adding Job", e));


                Intent intent1 = new Intent(context, MainMenuActivity.class);
                intent1.putExtra("User", currUserPost);
                context.startActivity(intent1);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });


    }

}