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

import com.orlando.greenworks.ScannedItems;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("History");
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your adapter with dummy/static data
        HistoryAdapter adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    static class HistoryHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.title);
            descriptionView = itemView.findViewById(R.id.description);
        }
    }

    static class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
        // For demo, specify a fixed number of items
        private static final int ITEM_COUNT = 10;

        @NonNull
        @Override
        public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_row, parent, false);
            return new HistoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
            // Here you would set the data for each item based on position
            // For demo purposes, using static data
            holder.imageView.setImageResource(R.drawable.cardboard); // Set your default or position-based image
            holder.titleView.setText("Cardboard"); // Set your title
            holder.descriptionView.setText("Recycled on: 2020-12-12"); // Set your description
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT; // Return the fixed item count
        }
    }
}
