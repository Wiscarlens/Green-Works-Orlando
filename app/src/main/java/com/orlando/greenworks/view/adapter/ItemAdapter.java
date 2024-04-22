package com.orlando.greenworks.view.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.orlando.greenworks.model.Item;
import com.orlando.greenworks.R;
import com.orlando.greenworks.view.fragments.ItemInformationFragment;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    private final ArrayList<Item> itemList;
    private final Context context;

    public ItemAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //  TODO: Turn this into a method
        String imageName = itemList.get(position).getItemImagePath();
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        String materialPoint = itemList.get(position).getItemPoint() + " points";

        holder.itemImage.setImageResource(imageResId);
        holder.itemName.setText(itemList.get(position).getItemName());
        holder.itemPoint.setText(materialPoint);

        holder.itemLayout.setOnClickListener(v -> {
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
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemName;
        private final TextView itemPoint;

        private final LinearLayout itemLayout;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImageIV);
            itemName = itemView.findViewById(R.id.itemNameTV);
            itemPoint = itemView.findViewById(R.id.itemPointsTV);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
