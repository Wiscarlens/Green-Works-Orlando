package com.orlando.greenworks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScannedItemAdapter extends RecyclerView.Adapter<ScannedItemAdapter.ViewHolder> {

    private final List<ScannedItems> scannedItems;

    public ScannedItemAdapter(List<ScannedItems> scannedItems) {
        this.scannedItems = scannedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameView;
        private final TextView scanDateView;
        private final ImageView itemImageView;

        public ViewHolder(View view) {
            super(view);
            itemNameView = view.findViewById(R.id.itemNameTV);
            scanDateView = view.findViewById(R.id.scan_date);
            itemImageView = view.findViewById(R.id.itemImageIV);
        }

        public void bind(ScannedItems item) {
            itemNameView.setText(item.getItemName());
            scanDateView.setText(item.getScanDate());
            itemImageView.setImageResource(item.getImageResourceId());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scanned_item_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(scannedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return scannedItems.size();
    }
}

