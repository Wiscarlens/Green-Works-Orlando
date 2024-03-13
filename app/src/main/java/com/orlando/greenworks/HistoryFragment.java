package com.orlando.greenworks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ScannedItems> itemsList = new ArrayList<>(); // Initialized with an empty list.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize with dummy data if there are no real scanned items.
        initializeDummyData();

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryAdapter adapter = new HistoryAdapter(itemsList); // Pass the list to the adapter.
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Method to initialize dummy data
    private void initializeDummyData() {
        // Replace "R.mipmap.ic_launcher" with the actual image resource you want to use for dummy items
        itemsList.add(new ScannedItems("Cardboard Box", "Recycled on: 01/10/2024", R.drawable.cardboard));
        itemsList.add(new ScannedItems("Paper", "Recycled on: 01/09/2024", R.drawable.paper));
        itemsList.add(new ScannedItems("Spindrift", "Recycled on: 01/08/2024", R.drawable.spindrift));
        itemsList.add(new ScannedItems("Dasani", "Recycled on: 01/07/2024", R.drawable.dasani));
        itemsList.add(new ScannedItems("Glass Bottle", "Recycled on: 01/06/2024", R.drawable.glass));
        itemsList.add(new ScannedItems("Water Bottle", "Recycled on: 01/05/2024", R.drawable.water_bottle));
        itemsList.add(new ScannedItems("Paper", "Recycled on: 01/04/2024", R.drawable.paper));
        itemsList.add(new ScannedItems("Glass Bottle", "Recycled on: 01/03/2024", R.drawable.glass));
        itemsList.add(new ScannedItems("Paper", "Recycled on: 01/02/2024", R.drawable.paper));
        itemsList.add(new ScannedItems("Water Bottle", "Recycled on: 01/01/2024", R.drawable.water_bottle));
        // Add as many dummy items as you want here.
    }

    private class HistoryHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView scanDate;

        public HistoryHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            scanDate = itemView.findViewById(R.id.scan_date);
        }
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
        private List<ScannedItems> itemsList;

        public HistoryAdapter(List<ScannedItems> itemsList) {
            this.itemsList = itemsList;
        }

        @NonNull
        @Override
        public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scanned_item_view, parent, false);
            return new HistoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
            ScannedItems item = itemsList.get(position);
            holder.itemImage.setImageResource(item.getImageResourceId());
            holder.itemName.setText(item.getItemName());
            holder.scanDate.setText(item.getScanDate());
        }

        @Override
        public int getItemCount() {
            return itemsList != null ? itemsList.size() : 0; // Avoid null pointer exception.
        }
    }
}

