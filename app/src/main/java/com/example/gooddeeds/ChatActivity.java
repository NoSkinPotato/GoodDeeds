package com.example.gooddeeds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ChatActivity extends AppCompatActivity {

    private User currUser;
    private User otherUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currUser = (User) getIntent().getSerializableExtra("User");
        otherUser = (User) getIntent().getSerializableExtra("OtherUser");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        RecyclerView chatBoxView = findViewById(R.id.ChatBoxView);
        chatBoxView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        executor.execute(() -> {
            List<Chat> messages = db.jobDao().getAllMessagesWithID(currUser.email, otherUser.email);

           mainHandler.post(() -> {
               if(!messages.isEmpty()){
                   Log.d("Jab", "Cunt1");
                   ChatBoxAdapter adapter = new ChatBoxAdapter(messages, currUser);
                   chatBoxView.setAdapter(adapter);

               }else{
                   Log.d("Jab", "Cunt2");
                   chatBoxView.setVisibility(View.INVISIBLE);
               }
           });
        });


        EditText inputField = findViewById(R.id.inputChat);
        ImageView enterBtn = findViewById(R.id.EnterChatBtn);

        enterBtn.setOnClickListener(e -> {
            if (inputField.getText().length() > 0){
                int newID = db.jobDao().getMaxChat(currUser.email, otherUser.email);
                String newChatID = currUser.email + "#" + otherUser.email;
                Chat newChat = new Chat(newChatID, newID + 1, inputField.getText().toString());
                db.jobDao().insertMessage(newChat);

                inputField.setText("");

                this.recreate();
            }
        });


        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, ChatMenuActivity.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        });

    }

}