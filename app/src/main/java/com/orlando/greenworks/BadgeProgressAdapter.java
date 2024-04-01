package com.orlando.greenworks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class BadgeProgressAdapter extends RecyclerView.Adapter<BadgeProgressAdapter.BadgeProgressViewHolder> {
    private final ArrayList<Badge> badgeList;
    private final Context context;

    public BadgeProgressAdapter(ArrayList<Badge> badgeList, Context context) {
        this.badgeList = badgeList;
        this.context = context;
    }
    @NonNull
    @Override
    public BadgeProgressAdapter.BadgeProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_progress_design, parent, false);

        return new BadgeProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeProgressAdapter.BadgeProgressViewHolder holder, int position) {
        int imageResId = context.getResources().getIdentifier(badgeList.get(position).getImagePath(), "drawable", context.getPackageName());
        String progressText = badgeList.get(position).getPoints() + "/" + badgeList.get(position).getMaxPoints();

        holder.badgeTitle.setText(badgeList.get(position).getTitle());
        holder.badgeImage.setImageResource(imageResId);
        holder.progressBar.setMax(badgeList.get(position).getMaxPoints());
        holder.progressBar.setProgress(badgeList.get(position).getPoints());
        holder.progressText.setText(progressText);
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public static class BadgeProgressViewHolder extends RecyclerView.ViewHolder {
        private final ImageView badgeImage;
        private final TextView badgeTitle;
        private final ProgressBar progressBar;
        private final TextView progressText;

        public BadgeProgressViewHolder(@NonNull View itemView) {
            super(itemView);

            badgeImage = itemView.findViewById(R.id.badgeImageIV);
            badgeTitle = itemView.findViewById(R.id.badgeTitleTV);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressText = itemView.findViewById(R.id.progressText);

        }
    }
}
