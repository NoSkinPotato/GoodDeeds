package com.example.gooddeeds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public static String formatNumberWithDots(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number).replace(",", ".");
    }

    private static final String TAG = "ItemAdapter";

    private List<Job> jobsList;
    private final Context context;
    private final User currUser;

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
        if (job == null) Log.d("Stupid", "THing");
        holder.jobTitle.setText(job.title);
        holder.jobDescription.setText(job.description);
        holder.jobDistance.setText(job.address);

        holder.background.setOnClickListener(e -> {
            Intent intent1 = new Intent(context, JobDetail.class);
            intent1.putExtra("User", currUser);
            intent1.putExtra("Job", job);
            context.startActivity(intent1);
        });

        if(job.reward == 0){
            holder.jobReward.setText("Volunteer Work");
        }else{
            String reward = "Rp " + formatNumberWithDots(job.reward);
            holder.jobReward.setText(reward);
        }

        if (job.userID.equals(currUser.userID)){

            holder.declineBtn.setVisibility(View.GONE);
            holder.acceptBtn.setVisibility(View.GONE);

        }else{
            holder.waitWorkers.setVisibility(View.GONE);
            holder.removeBtn.setVisibility(View.GONE);
        }

        ViewJob(context, job, holder.removeBtn, holder.declineBtn, holder.acceptBtn);

    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    private void ViewJob(Context context, Job currJob, Button removeBtn, Button declineBtn, Button acceptBtn){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference jobRef = database.getReference("Job");
        DatabaseReference declineJobRef = database.getReference("DeclineJob");

        removeBtn.setOnClickListener(func -> {
            Log.d("WOIIII", "Gay ass " + currJob.id);
            jobRef.child(currJob.id).removeValue()
                    .addOnSuccessListener(e -> {
                        Log.d(TAG, "Job Deleted Successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Job Failed To Delete");
                    });

            Intent intent = new Intent(context, MainMenuActivity.class);
            intent.putExtra("User", currUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });



        declineBtn.setOnClickListener(func -> {

            DeclinedJob dj = new DeclinedJob(currUser.userID, currJob.id);
            String declineID = dj.userID + "|" + dj.id;
            declineJobRef.child(declineID).setValue(dj)
                    .addOnSuccessListener(e -> {
                        Log.d(TAG, "Job Declined Successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Failed To Decline Job");
                    });

            Intent intent = new Intent(context, MainMenuActivity.class);
            intent.putExtra("User", currUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });

        acceptBtn.setOnClickListener(func -> {

            Map<String, Object> update = new HashMap<>();
            update.put("workerID", currUser.userID);

            jobRef.child(currJob.id).updateChildren(update)
                    .addOnSuccessListener(e -> {
                        Log.d(TAG, "Worker ID Updated");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Fail To Update Worker ID");
                    });

            Intent intent = new Intent(context, ChatMenuActivity.class);
            intent.putExtra("User", currUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }

}
