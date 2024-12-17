package com.example.gooddeeds;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.ChatBoxViewHolder>{

    private List<Chat> allMessages;

    public ChatBoxAdapter(List<Chat> allMessages) {
        this.allMessages = allMessages;
    }

    public static class ChatBoxViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        public ChatBoxViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageChat);
        }
    }

    @NonNull
    @Override
    public ChatBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatbox_layout, parent, false);
        return new ChatBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBoxViewHolder holder, int position) {
        Chat chat = allMessages.get(position);

        holder.message.setText(chat.text);

    }
    @Override
    public int getItemCount() {
        return allMessages.size();
    }

}
