package com.example.gooddeeds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatMenuActivity extends AppCompatActivity {

    private User currUser;
    private static final String TAG = "ChatMenuActivity";
    private RecyclerView chatView;
    private List<User> users = new ArrayList<>();

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

        chatView = findViewById(R.id.ChatView);
        chatView.setLayoutManager(new LinearLayoutManager(this));

        GenerateUserChats(this);

        LinearLayout homeBtn = findViewById(R.id.homeButton);

        homeBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("User", currUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        LinearLayout profBtn = findViewById(R.id.profileButton);

        profBtn.setOnClickListener(e -> {
            Intent intent  = new Intent(this, Profile_Activity.class);
            intent.putExtra("User", currUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void GenerateUserChats(Context context){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference jobRef = database.getReference("Job");

        jobRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> allUserID = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()){
                    Job job = data.getValue(Job.class);

                    if (job.workerID == null) continue;

                    if (Objects.equals(job.userID, currUser.userID) || Objects.equals(job.workerID, currUser.userID)){

                        String ID = Objects.equals(job.userID, currUser.userID) ? job.workerID : job.userID;

                        allUserID.add(ID);
                    }
                }

                Set<String> set = new HashSet<>(allUserID);
                List<String> allUserID2 = new ArrayList<>(set);

                users.clear();
                DatabaseReference userRef = database.getReference("User");

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (String s : allUserID2){
                            for (DataSnapshot data : snapshot.getChildren()){
                                if (Objects.equals(data.getKey(), s)){
                                    users.add(data.getValue(User.class));
                                    break;
                                }
                            }
                        }

                        if(!users.isEmpty()){
                            ChatAdapter adapter = new ChatAdapter(users, currUser, context);
                            chatView.setAdapter(adapter);
                        }else{
                            chatView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "User Cancelled");
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