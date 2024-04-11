package com.orlando.greenworks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class HistoryFragment extends Fragment {

    private final ArrayList<Item> itemList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.fragmentTitle.setText(R.string.history); // Set the title of the fragment in the toolbar.

        // Initialize with dummy data if there are no real scanned items.
        initializeDummyData();

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.historyRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryAdapter adapter = new HistoryAdapter(itemList, getContext()); // Pass the list to the adapter.
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Method to initialize dummy data
    private void initializeDummyData() {
        itemList.add(
                new Item(
                        "cardboard",
                        "Cardboard",
                        "Recycling cardboard is a simple process that involves breaking down the material into smaller pieces and placing it in a recycling bin. Cardboard can be recycled multiple times and is a valuable material for creating new products.",
                        "Cardboard",
                        13,
                        "January 9, 2024"
                )
        );

        itemList.add(
                new Item(
                        "paper",
                        "Paper",
                        "Recycling paper involves breaking down the material into smaller pieces and placing it in a recycling bin. Paper can be recycled multiple times and is a valuable material for creating new products.",
                        "Paper",
                        9,
                        "April 27, 2024"
                )
        );

        itemList.add(
                new Item(
                        "spindrift",
                        "Spindrift",
                        "Recycling an aluminum can involves cleaning the can and placing it in a recycling bin designated for aluminum materials. Aluminum is a valuable material that can be recycled multiple times without losing its quality.",
                        "Aluminum",
                        16,
                        "January 8, 2024"
                )
        );

        itemList.add(
                new Item(
                        "dasani",
                        "Dasani",
                        "Recycling a Dasani water bottle begins with emptying any remaining liquid and removing the cap. Once cleaned, the bottle can be placed in a recycling bin designated for plastic materials. ",
                        "PET",
                        7,
                        "January 10, 2024"
                )
        );

        itemList.add(
                new Item(
                        "glass",
                        "Glass",
                        "Recycling glass involves cleaning the material and placing it in a recycling bin designated for glass materials. Glass is a valuable material that can be recycled multiple times without losing its quality.",
                        "Glass",
                        14,
                        "March 12, 2024"
                )
        );

        itemList.add(
                new Item(
                        "batteries",
                        "Batteries",
                        "Recycling batteries involves taking them to a designated recycling center or drop-off location. Batteries contain toxic materials that can harm the environment if not disposed of properly.",
                        "Batteries",
                        7,
                        "January 10, 2024"
                )
        );

    }

    @Override
    public void onStop() {
        super.onStop();

        MainActivity.fragmentTitle.setText(""); // Clear the title of the fragment in the toolbar.

    }
}

