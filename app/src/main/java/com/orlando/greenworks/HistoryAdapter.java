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

/**
 * The HistoryAdapter class extends the RecyclerView.Adapter class.
 * It represents an adapter for a RecyclerView that displays a list of items in the history.
 * The adapter binds the data to the views in the RecyclerView.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private final ArrayList<Item> itemList;
    private final Context context;

    /**
     * The constructor for the HistoryAdapter class.
     * @param itemList The list of items in the history.
     * @param context The context of the adapter.
     */
    public HistoryAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    /**
     * This method creates a new view holder for the RecyclerView.
     * @param parent The ViewGroup into which the new view will be added after it is bound to an adapter position.
     * @param viewType The view type of the new view.
     * @return The new view holder.
     */
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_design, parent, false);

        return new HistoryHolder(view);
    }

    /**
     * This method binds the data to the views in the view holder.
     * It sets the image, name, material, points, and date of the item in the view holder.
     * It also sets the click listener for the layout of the item.
     * @param holder The view holder.
     * @param position The position of the item in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        String imageName = itemList.get(position).getItemImagePath();
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

    /**
     * This method returns the number of items in the RecyclerView.
     * @return The number of items in the RecyclerView.
     */
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

        /**
         * The HistoryHolder class extends the RecyclerView.ViewHolder class.
         * It represents a view holder for an item in the RecyclerView.
         * The view holder includes an image view for the item image, text views for the item name, material, points, and date, and a layout for the item.
         */

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
