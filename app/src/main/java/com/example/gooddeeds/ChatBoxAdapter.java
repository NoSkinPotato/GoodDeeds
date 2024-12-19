package com.example.gooddeeds;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.ChatBoxViewHolder>{

    private List<Chat> allMessages;

    private User currUser;

    public ChatBoxAdapter(List<Chat> allMessages, User currUser) {
        this.allMessages = allMessages;
        this.currUser = currUser;
    }

    public static class ChatBoxViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        MaterialCardView messageBox;
        public ChatBoxViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageChatText);
            messageBox = itemView.findViewById(R.id.messageChatBox);
        }
    }

    @NonNull
    @Override
    public ChatBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatbox_layout, parent, false);
        return new ChatBoxViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ChatBoxViewHolder holder, int position) {
        Chat chat = allMessages.get(position);

        holder.message.setText(chat.text);

        if (chat.chatID.startsWith(currUser.email)){
            //FromUser
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageBox.getLayoutParams();
            params.gravity = Gravity.END;
            holder.messageBox.setLayoutParams(params);

            holder.messageBox.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            holder.message.setTextColor(Color.WHITE);
        }else{
            //FromOtherUser
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageBox.getLayoutParams();
            params.gravity = Gravity.START;
            holder.messageBox.setLayoutParams(params);

            holder.message.setTextColor(Color.BLACK);
            holder.messageBox.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64C3BF")));
        }



    }
    @Override
    public int getItemCount() {
        return allMessages.size();
    }

}
