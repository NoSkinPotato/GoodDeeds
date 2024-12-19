package com.example.gooddeeds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatMenuActivity extends AppCompatActivity {

    private User currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currUser = (User) getIntent().getSerializableExtra("User");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        RecyclerView chatView = findViewById(R.id.ChatView);
        chatView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        executor.execute(() -> {
            List<User> users = db.jobDao().getAllChatUsers(currUser.email);

            mainHandler.post(() -> {
                if(!users.isEmpty()){
                    Log.d("JIPIPIPI", "Stupid");
                    ChatAdapter adapter = new ChatAdapter(users, currUser, this, db);
                    chatView.setAdapter(adapter);
                }else{
                    Log.d("GABABA", "Stupid");
                    chatView.setVisibility(View.INVISIBLE);
                }
            });

        });

        LinearLayout homeBtn = findViewById(R.id.homeButton);

        homeBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });

        LinearLayout profBtn = findViewById(R.id.profileButton);

        profBtn.setOnClickListener(e -> {
            Intent intent  = new Intent(this, Profile_Activity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });


    }
}