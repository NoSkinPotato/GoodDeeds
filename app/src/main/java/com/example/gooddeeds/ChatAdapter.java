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

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<User> allUsers;
    private User currUser;
    private Context context;

    public ChatAdapter(List<User> allUsers, User currUser, Context context) {
        this.allUsers = allUsers;
        this.currUser = currUser;
        this.context = context;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        LinearLayout chatContainer;
        public ChatViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            chatContainer = itemView.findViewById(R.id.ChatContainer);
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

}
