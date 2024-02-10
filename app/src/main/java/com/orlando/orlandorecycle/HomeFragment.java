package com.orlando.orlandorecycle;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import java.util.Objects;

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
        ImageView weeklyStatsImage = view.findViewById(R.id.weeklyStatsIV);
        TextView weeklyStatsPoints = view.findViewById(R.id.weeklyStatsTV);

        LinearLayout monthlyStats = view.findViewById(R.id.monthlyStatsLL);
        ImageView monthlyStatsImage = view.findViewById(R.id.monthlyStatsIV);
        TextView monthlyStatsPoints = view.findViewById(R.id.monthlyStatsTV);

        LinearLayout yearlyStats = view.findViewById(R.id.yearlyStatsLL);
        ImageView yearlyStatsImage = view.findViewById(R.id.yearlyStatsIV);
        TextView yearlyStatsPoints = view.findViewById(R.id.yearlyStatsTV);


        TextView statsTopOnePoints = view.findViewById(R.id.statsTopOnePointsTV);
        TextView statsTopOneMaterial = view.findViewById(R.id.statsTopOneMaterialTV);
        TextView statsTopTwoPoints = view.findViewById(R.id.statsTopTwoPointsTV);
        TextView statsTopTwoMaterial = view.findViewById(R.id.statsTopTwoMaterialTV);
        TextView statsTopThreePoints = view.findViewById(R.id.statsTopThreePointsTV);
        TextView statsTopThreeMaterial = view.findViewById(R.id.statsTopThreeMaterialTV);

        RecyclerView recyclerView = view.findViewById(R.id.lastScannedRV);

        ItemAdapter itemAdapter;
        ArrayList<Item> itemList = new ArrayList<>();

        weeklyStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weeklyStats.setBackgroundResource(R.drawable.circle_selection);
                monthlyStats.setBackgroundResource(R.drawable.circle);
                yearlyStats.setBackgroundResource(R.drawable.circle);

                // TODO: create a variable for the colors
                weeklyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue));
                weeklyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue));

                monthlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.dark_bleu));
                monthlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_bleu));

                yearlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.dark_bleu));
                yearlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_bleu));

                statsTopOnePoints.setText("20");
                statsTopOneMaterial.setText("Textile");

                statsTopTwoPoints.setText("16");
                statsTopTwoMaterial.setText("Aluminum");

                statsTopThreePoints.setText("14");
                statsTopThreeMaterial.setText("Glass");

            }
        });

        monthlyStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weeklyStats.setBackgroundResource(R.drawable.circle);
                monthlyStats.setBackgroundResource(R.drawable.circle_selection);
                yearlyStats.setBackgroundResource(R.drawable.circle);

                weeklyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.dark_bleu));
                weeklyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_bleu));

                monthlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue));
                monthlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue));

                yearlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.dark_bleu));
                yearlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_bleu));

                statsTopOnePoints.setText("80");
                statsTopOneMaterial.setText("Cardboard");

                statsTopTwoPoints.setText("64");
                statsTopTwoMaterial.setText("Aluminum");

                statsTopThreePoints.setText("56");
                statsTopThreeMaterial.setText("Plastic");
            }
        });

        yearlyStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weeklyStats.setBackgroundResource(R.drawable.circle);
                monthlyStats.setBackgroundResource(R.drawable.circle);
                yearlyStats.setBackgroundResource(R.drawable.circle_selection);

                weeklyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.dark_bleu));
                weeklyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_bleu));

                monthlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.dark_bleu));
                monthlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_bleu));

                yearlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue));
                yearlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue));

                statsTopOnePoints.setText("960");
                statsTopOneMaterial.setText("Cardboard");

                statsTopTwoPoints.setText("768");
                statsTopTwoMaterial.setText("Aluminum");

                statsTopThreePoints.setText("672");
                statsTopThreeMaterial.setText("Plastic");

            }
        });

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