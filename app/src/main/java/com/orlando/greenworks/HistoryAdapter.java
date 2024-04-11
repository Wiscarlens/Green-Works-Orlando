package com.orlando.greenworks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private final ArrayList<Item> itemList;
    private final Context context;

    public HistoryAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_design, parent, false);

        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        String imageName = itemList.get(position).getItemImage();
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        String materialPoint = itemList.get(position).getItemPoint() + " points";

        holder.itemImage.setImageResource(imageResId);
        holder.itemName.setText(itemList.get(position).getItemName());
        holder.itemMaterial.setText(itemList.get(position).getItemMaterial());
        holder.itemPoint.setText(materialPoint);
        holder.scanDate.setText(itemList.get(position).getItemDate());

        holder.historyLayout.setOnClickListener(v -> {
                    Item item = itemList.get(position);  // Get the item that was clicked

                    // Create a new instance of ItemInformationFragment with the item
                    ItemInformationFragment itemInformationFragment = ItemInformationFragment.newInstance(item, false);

                    // Show the fragment
                    itemInformationFragment.show(((AppCompatActivity) context)
                            .getSupportFragmentManager(), itemInformationFragment.getTag());

                }
        );
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0; // Avoid null pointer exception.
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemName;
        private final TextView itemMaterial;
        private final TextView itemPoint;
        private final TextView scanDate;
        private LinearLayout historyLayout;

        public HistoryHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemMaterial = itemView.findViewById(R.id.item_material);
            itemPoint = itemView.findViewById(R.id.item_points);
            scanDate = itemView.findViewById(R.id.scan_date);
            historyLayout = itemView.findViewById(R.id.history_item_layout);
        }
    }
}
