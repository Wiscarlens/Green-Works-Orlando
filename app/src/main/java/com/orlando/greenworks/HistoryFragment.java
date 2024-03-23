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

    private final ArrayList<Item> itemsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.fragmentTitle.setText(R.string.history); // Set the title of the fragment in the toolbar.

        // Initialize with dummy data if there are no real scanned items.
        initializeDummyData();

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryAdapter adapter = new HistoryAdapter(itemsList, getContext()); // Pass the list to the adapter.
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Method to initialize dummy data
    private void initializeDummyData() {
        itemsList.add(new Item("cardboard", "Cardboard Box", "Cardboard", 10, "01/10/2024"));
        itemsList.add(new Item("paper", "Paper", "Paper", 5, "01/09/2024"));
        itemsList.add(new Item("spindrift", "Spindrift", "Plastic", 5, "01/08/2024"));
        itemsList.add(new Item("dasani", "Dasani", "Plastic", 5, "01/07/2024"));
        itemsList.add(new Item("glass", "Glass Bottle", "Glass", 10, "01/06/2024"));
        itemsList.add(new Item("water_bottle", "Water Bottle", "Plastic", 5, "01/05/2024"));

    }

    @Override
    public void onStop() {
        super.onStop();

        MainActivity.fragmentTitle.setText(""); // Clear the title of the fragment in the toolbar.

    }
}

