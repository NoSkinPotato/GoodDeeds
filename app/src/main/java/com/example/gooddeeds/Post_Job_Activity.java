package com.example.gooddeeds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class Post_Job_Activity extends AppCompatActivity {

    private User currUserPost;

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

        TextView title = findViewById(R.id.jobTitleInput);
        TextView reward = findViewById(R.id.jobRewardInput);
        TextView description = findViewById(R.id.jobDescInput);

        Button postJob = findViewById(R.id.PostJob);

        postJob.setOnClickListener(func -> {

            int rew;
            if(reward.getText().toString().isEmpty())
                rew = 0;
            else
                rew = Integer.parseInt(reward.getText().toString());

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            JobClassDAO dao = db.jobDao();

            String id;
            while (true){
                String x = getRandomString(3);
                Job job = dao.CheckJobID(x);
                if (job == null){
                    id = x;
                    break;
                }
            }

            Job newJob = new Job(currUserPost.email,id, title.getText().toString(), description.getText().toString(), currUserPost.address, rew,  null);
            dao.insertJob(newJob);

            Intent intent1 = new Intent(this, MainMenuActivity.class);
            intent1.putExtra("User", currUserPost);
            startActivity(intent1);

        });

        ImageView backHome = findViewById(R.id.BackHome);

        backHome.setOnClickListener(func -> {
            Intent intent1 = new Intent(this, MainMenuActivity.class);
            intent1.putExtra("User", currUserPost);
            startActivity(intent1);
        });


    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}