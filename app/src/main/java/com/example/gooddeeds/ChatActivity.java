package com.example.gooddeeds;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {

    private User currUser;
    private User otherUser;
    private RecyclerView chatBoxView;
    private static final String TAG = "ChatActivity";
    private String id = null;
    private List<Chat> messages;
    private ChatBoxAdapter adapter;
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
        chatBoxView = findViewById(R.id.ChatBoxView);
        chatBoxView.setLayoutManager(new LinearLayoutManager(this));

        GenerateMessages();

        TextView userName = findViewById(R.id.userName);
        userName.setText(otherUser.firstName + " " + otherUser.lastName);
        EditText inputField = findViewById(R.id.inputChat);
        ImageView enterBtn = findViewById(R.id.EnterChatBtn);

        enterBtn.setOnClickListener(e -> {
            if (inputField.getText().length() > 0){
                CreateNewChat(this, id, enterBtn, inputField, adapter);
            }
        });

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, ChatMenuActivity.class);
            intent.putExtra("User", currUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
    private void GenerateMessages() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chat");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()){
                    id = data.getKey();
                    if (id.contains(currUser.userID) && id.contains(otherUser.userID)){
                        break;
                    }
                }

                if (id == null) return;

                myRef.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()){
                            messages.add(data.getValue(Chat.class));
                        }

                        messages.sort(new Comparator<Chat>() {
                            @Override
                            public int compare(Chat o1, Chat o2) {
                                return Integer.compare(o1.textID, o2.textID);
                            }
                        });

                        if(!messages.isEmpty()){
                            adapter = new ChatBoxAdapter(messages, currUser);
                            chatBoxView.setAdapter(adapter);
                            chatBoxView.scrollToPosition(adapter.getItemCount()-1);
                        }else{
                            chatBoxView.setVisibility(View.INVISIBLE);
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

    private void CreateNewChat(Context context, String id, ImageView enterBtn, EditText inputField, ChatBoxAdapter adapter) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chat");

        String x = id == null ? "x" : id;

        myRef.child(x).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    int largestValue = 0;
                    for (DataSnapshot data : snapshot.getChildren()){
                        if (data == null) continue;

                        if (data.getValue(Chat.class).textID > largestValue){
                            largestValue = data.getValue(Chat.class).textID;
                        }
                    }

                    Chat newChat = new Chat(id, largestValue+1, currUser.userID, inputField.getText().toString());

                    myRef.child(id).child(String.valueOf(largestValue+1)).setValue(newChat)
                                    .addOnSuccessListener(e->{
                                        Log.d(TAG, "Chat Successfully Saved");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d(TAG, "Chat Failed To Save");
                                    });
                    messages.add(newChat);

                }else{

                    String newID = currUser.userID + "|" + otherUser.userID;
                    Chat newChat = new Chat(newID, 0, currUser.userID, inputField.getText().toString());
                    myRef.child(newID).child(String.valueOf(0)).setValue(newChat)
                            .addOnSuccessListener(e->{
                                Log.d(TAG, "Chat Successfully Saved");
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "Chat Failed To Save");
                            });
                    messages.add(newChat);
                }

                inputField.setText("");
                adapter.updateList(messages);
                chatBoxView.scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }



}