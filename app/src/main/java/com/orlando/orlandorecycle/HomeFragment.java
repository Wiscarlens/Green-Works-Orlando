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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private LinearLayout weeklyStats;
    private ImageView weeklyStatsImage;
    private TextView weeklyStatsPoints;

    private LinearLayout monthlyStats;
    private ImageView monthlyStatsImage;
    private TextView monthlyStatsPoints;

    private LinearLayout yearlyStats;
    private ImageView yearlyStatsImage;
    private TextView yearlyStatsPoints;

    private TextView statsTopOnePoints;
    private TextView statsTopOneMaterial;
    private TextView statsTopTwoPoints;
    private TextView statsTopTwoMaterial;
    private TextView statsTopThreePoints;
    private TextView statsTopThreeMaterial;


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

        weeklyStats = view.findViewById(R.id.weeklyStatsLL);
        weeklyStatsImage = view.findViewById(R.id.weeklyStatsIV);
        weeklyStatsPoints = view.findViewById(R.id.weeklyStatsTV);

        monthlyStats = view.findViewById(R.id.monthlyStatsLL);
        monthlyStatsImage = view.findViewById(R.id.monthlyStatsIV);
        monthlyStatsPoints = view.findViewById(R.id.monthlyStatsTV);

        yearlyStats = view.findViewById(R.id.yearlyStatsLL);
        yearlyStatsImage = view.findViewById(R.id.yearlyStatsIV);
        yearlyStatsPoints = view.findViewById(R.id.yearlyStatsTV);


        statsTopOnePoints = view.findViewById(R.id.statsTopOnePointsTV);
        statsTopOneMaterial = view.findViewById(R.id.statsTopOneMaterialTV);
        statsTopTwoPoints = view.findViewById(R.id.statsTopTwoPointsTV);
        statsTopTwoMaterial = view.findViewById(R.id.statsTopTwoMaterialTV);
        statsTopThreePoints = view.findViewById(R.id.statsTopThreePointsTV);
        statsTopThreeMaterial = view.findViewById(R.id.statsTopThreeMaterialTV);

        RecyclerView recyclerView = view.findViewById(R.id.lastScannedRV);

        ItemAdapter itemAdapter;
        ArrayList<Item> itemList = new ArrayList<>();

        int circleSelection = R.drawable.circle_selection;
        int circle = R.drawable.circle;
        int lightBlue = R.color.light_blue;
        int darkBlue = R.color.dark_bleu;

        weeklyStats.setOnClickListener(v -> {
            setBackgroundResources(weeklyStats, monthlyStats, yearlyStats, circleSelection, circle, circle);

            setColorFilters(weeklyStatsImage, monthlyStatsImage, yearlyStatsImage, lightBlue, darkBlue, darkBlue);
            setTextColors(weeklyStatsPoints, monthlyStatsPoints, yearlyStatsPoints, lightBlue, darkBlue, darkBlue);

            setTextValues("20", "Textile",
                    "16", "Aluminum",
                    "14", "Glass"
            );
        });

        monthlyStats.setOnClickListener(v -> {
            setBackgroundResources(weeklyStats, monthlyStats, yearlyStats, circle, circleSelection, circle);

            setColorFilters(weeklyStatsImage, monthlyStatsImage, yearlyStatsImage,  darkBlue, lightBlue, darkBlue);
            setTextColors(weeklyStatsPoints, monthlyStatsPoints, yearlyStatsPoints, darkBlue, lightBlue, darkBlue);

            setTextValues("80", "Cardboard",
                    "64", "Aluminum",
                    "56", "Glass"
            );

        });

        yearlyStats.setOnClickListener(v -> {
            setBackgroundResources(weeklyStats, monthlyStats, yearlyStats, circle, circle, circleSelection);

            setColorFilters(weeklyStatsImage, monthlyStatsImage, yearlyStatsImage,  darkBlue, darkBlue, lightBlue);
            setTextColors(weeklyStatsPoints, monthlyStatsPoints, yearlyStatsPoints, darkBlue, darkBlue, lightBlue);

            setTextValues("960", "Cardboard",
                    "768", "Aluminum",
                    "672", "Glass"
            );

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
                        "batteries"
                ));



        itemAdapter = new ItemAdapter(itemList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);


        profileImage.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.show(getParentFragmentManager(), profileFragment.getTag());
        });



        sortingGuideButton.setOnClickListener(v -> {
            SortingGuideFragment sortingGuideFragment = new SortingGuideFragment();
            sortingGuideFragment.show(getParentFragmentManager(), sortingGuideFragment.getTag());
        });

        return view;
    }

    private void setBackgroundResources(LinearLayout weeklyStats, LinearLayout monthlyStats, LinearLayout yearlyStats, int weeklyStatsBackground, int monthlyStatsBackground, int yearlyStatsBackground) {
        weeklyStats.setBackgroundResource(weeklyStatsBackground);
        monthlyStats.setBackgroundResource(monthlyStatsBackground);
        yearlyStats.setBackgroundResource(yearlyStatsBackground);
    }

    private void setColorFilters(ImageView weeklyStatsImage, ImageView monthlyStatsImage, ImageView yearlyStatsImage, int weeklyColor, int monthlyColor, int yearlyColor) {
        weeklyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), weeklyColor));
        monthlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), monthlyColor));
        yearlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), yearlyColor));
    }

    private void setTextColors(@NonNull TextView weeklyStatsPoints, TextView monthlyStatsPoints, TextView yearlyStatsPoints, int weeklyColor, int monthlyColor, int yearlyColor) {
        weeklyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), weeklyColor));
        monthlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), monthlyColor));
        yearlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), yearlyColor));
    }

    private void setTextValues(String topOnePoints, String topOneMaterial, String topTwoPoints, String topTwoMaterial, String topThreePoints, String topThreeMaterial) {
        statsTopOnePoints.setText(topOnePoints);
        statsTopOneMaterial.setText(topOneMaterial);

        statsTopTwoPoints.setText(topTwoPoints);
        statsTopTwoMaterial.setText(topTwoMaterial);

        statsTopThreePoints.setText(topThreePoints);
        statsTopThreeMaterial.setText(topThreeMaterial);
    }
}