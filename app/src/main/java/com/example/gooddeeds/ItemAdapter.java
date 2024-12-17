package com.example.gooddeeds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public static String formatNumberWithDots(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number).replace(",", ".");
    }
    private List<Job> jobsList;
    private Context context;
    private User currUser;

    public ItemAdapter(Context ctx, User currUser, List<Job> jobList) {

        this.context = ctx;
        this.currUser = currUser;
        this.jobsList = jobList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle;
        TextView jobDistance;
        TextView jobDescription;
        TextView jobReward;
        TextView waitWorkers;
        Button declineBtn;
        Button acceptBtn;
        Button removeBtn;

        LinearLayout background;
        LinearLayout jobButtons;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.JobTitleCard);
            jobDistance = itemView.findViewById(R.id.JobDistanceCard);
            jobDescription = itemView.findViewById(R.id.JobDescCard);
            jobReward = itemView.findViewById(R.id.rewardText);
            waitWorkers = itemView.findViewById(R.id.waitWorkers);
            declineBtn = itemView.findViewById(R.id.declineButton);
            acceptBtn = itemView.findViewById(R.id.AcceptButton);
            removeBtn = itemView.findViewById(R.id.removeJobBtn);
            background = itemView.findViewById(R.id.JobBackground);
            jobButtons = itemView.findViewById(R.id.JobButtons);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.job_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobsList.get(position);
        holder.jobTitle.setText(job.title);
        holder.jobDescription.setText(job.description);
        holder.jobDistance.setText(job.address);

        if(job.reward == 0){
            holder.jobReward.setText("None");
        }else{
            String reward = "Rp " + formatNumberWithDots(job.reward);
            holder.jobReward.setText(reward);
        }

        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        JobClassDAO dao = db.jobDao();

        if (job.userGmail.equals(currUser.email)){

            holder.declineBtn.setVisibility(View.GONE);
            holder.acceptBtn.setVisibility(View.GONE);

            holder.removeBtn.setOnClickListener(func -> {

                dao.deleteJobByID(job.id);

                Intent intent = new Intent(context, MainMenuActivity.class);
                intent.putExtra("User", currUser);
                context.startActivity(intent);
            });


        }else{

            holder.waitWorkers.setVisibility(View.GONE);
            holder.removeBtn.setVisibility(View.GONE);



        }


    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

}
