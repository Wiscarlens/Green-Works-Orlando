package com.orlando.greenworks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.orlando.greenworks.ScannedItems;

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
    private ScannedItemAdapter adapter;
    private List<ScannedItems> scannedItems = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        List<ScannedItems> items = new ArrayList<>();
        items.add(new ScannedItems("Item 1", "2022-07-21", R.drawable.cardboard));


        adapter = new ScannedItemAdapter(items);
        recyclerView.setAdapter(adapter);

        return view;
    }
}