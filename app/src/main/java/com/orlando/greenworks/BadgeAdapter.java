package com.orlando.greenworks;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {
    private final ArrayList<Badge> badgeList;
    private final Context context;

    public BadgeAdapter(ArrayList<Badge> badgeList, Context context) {
        this.badgeList = badgeList;
        this.context = context;
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_design, parent, false);

        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        int imageResId = context.getResources().getIdentifier(badgeList.get(position).getImagePath(), "drawable", context.getPackageName());

        holder.badgeTitle.setText(badgeList.get(position).getTitle());
        holder.badgeImage.setImageResource(imageResId);
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView badgeImage;
        private final TextView badgeTitle;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);

            badgeImage = itemView.findViewById(R.id.badgeImageIV);
            badgeTitle = itemView.findViewById(R.id.badgeTitleTV);

        }
    }
}
