package com.example.gooddeeds;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle;

        public ChatViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.JobTitleCard);
        }
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_layout, parent, false);
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
