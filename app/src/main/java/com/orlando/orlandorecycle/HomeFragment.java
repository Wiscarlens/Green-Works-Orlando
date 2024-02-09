package com.orlando.orlandorecycle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView rewardsPoint = view.findViewById(R.id.rewardsPointsTV);
        ImageView profileImage = view.findViewById(R.id.profileImageIV);
        TextView userFirstName = view.findViewById(R.id.userFirstNameTV);
        SearchView searchView = view.findViewById(R.id.searchView);
        ImageButton sortingGuideButton = view.findViewById(R.id.sortingGuideIB);
        LinearLayout weeklyStats = view.findViewById(R.id.weeklyStatsLL);
        LinearLayout monthlyStats = view.findViewById(R.id.monthlyStatsLL);
        LinearLayout yearlyStats = view.findViewById(R.id.yearlyStatsLL);
        RecyclerView recyclerView = view.findViewById(R.id.lastScannedRV);
        ItemAdapter itemAdapter;
        ArrayList<Item> itemList = new ArrayList<>();

        itemList.add(
                new Item(
                        "Dasani",
                        "Plastic bottle",
                        7,
                        "dasani"
                ));
        itemList.add(
                new Item(
                        "Cardboard",
                        "Cardboard",
                        13,
                        "cardboard"
                ));
        itemList.add(
                new Item(
                        "Spindrift",
                        "Aluminum can",
                        16,
                        "spindrift"
                ));

        itemList.add(
                new Item(
                        "Paper",
                        "Paper",
                        9,
                        "paper"
                ));
        itemList.add(
                new Item(
                        "Glass",
                        "Glass",
                        14,
                        "glass"
                ));
        itemList.add(
                new Item(
                        "Textile",
                        "Textile",
                        20,
                        "textile"
                ));
        itemList.add(
                new Item(
                        "Batteries",
                        "Batteries",
                        7,
                        "batterie"
                ));



        itemAdapter = new ItemAdapter(itemList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.show(getParentFragmentManager(), profileFragment.getTag());
            }
        });



        sortingGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortingGuideFragment sortingGuideFragment = new SortingGuideFragment();
                sortingGuideFragment.show(getParentFragmentManager(), sortingGuideFragment.getTag());
            }
        });

        return view;
    }
}