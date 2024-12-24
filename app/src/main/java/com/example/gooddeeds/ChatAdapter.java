package com.example.gooddeeds;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<User> allUsers;
    private User currUser;
    private Context context;
    private static final String TAG = "ChatAdapter";

    public ChatAdapter(List<User> allUsers, User currUser, Context context) {
        this.allUsers = allUsers;
        this.currUser = currUser;
        this.context = context;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        LinearLayout chatContainer;
        TextView lastText;
        public ChatViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            chatContainer = itemView.findViewById(R.id.ChatContainer);
            lastText = itemView.findViewById(R.id.lastText);
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_layout, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        User user = allUsers.get(position);
        holder.userName.setText(user.firstName + " " + user.lastName);

        GetLastText(user, holder.lastText);

        holder.chatContainer.setOnClickListener(e -> {

            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("OtherUser", user);
            intent.putExtra("User", currUser);
            context.startActivity(intent);

        });

    }
    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    private void GetLastText(User user, TextView lastText){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chat");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               List<Chat> messages = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()){
                    String key = data.getKey();
                    if (key == null) continue;
                    Log.d(TAG, "Fine3");
                    String[] keyArray = key.split("|");

                    if ((Objects.equals(keyArray[0], user.userID) || Objects.equals(keyArray[1], currUser.userID))
                        || (Objects.equals(keyArray[1], user.userID) || Objects.equals(keyArray[0], currUser.userID))){
                       messages.add(data.getValue(Chat.class));
                    }
                }

                messages.sort(new Comparator<Chat>() {
                    @Override
                    public int compare(Chat o1, Chat o2) {
                        return Integer.compare(o1.textID, o2.textID);
                    }
                });
                if (!messages.isEmpty()){
                    String finalText = messages.get(messages.size()).toString();
                    lastText.setVisibility(View.VISIBLE);
                    lastText.setText(finalText);
                }else{
                    lastText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
