package com.orlando.greenworks;

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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import android.util.Log;

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

    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    private DatabaseHelper db;
    private TextView userFirstNameTV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userFirstNameTV = view.findViewById(R.id.userFirstNameTV);
        db = new DatabaseHelper(getContext()); // Initialize the DatabaseHelper
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String email = sharedPreferences.getString("email", "");
            Log.d("HomeFragment", "Email: " + email);
            String[] columns = {"first_name"};
            String selection = "email_address=?";
            String[] selectionArgs = {email};
            Cursor cursor = db.getReadableDatabase().query("User", columns, selection, selectionArgs, null, null, null);
            int columnIndex = cursor.getColumnIndex("first_name");
            Log.d("HomeFragment", "First name column index: " + columnIndex);
            if (columnIndex != -1 && cursor.moveToFirst()) {
                String firstName = cursor.getString(columnIndex);
                Log.d("HomeFragment", "First name: " + firstName);
                userFirstNameTV.setText(firstName);
            } else {
                Log.d("HomeFragment", "No user found with email: " + email);
            }
            Log.d("HomeFragment", "Cursor count: " + cursor.getCount());
            cursor.close();
        } else {
            Log.d("HomeFragment", "Is logged in: false");
        }


        MainActivity.fragmentTitle.setText(""); // Set the title of the fragment in the toolbar.

        LinearLayout reward = view.findViewById(R.id.rewardsLayout);
        TextView rewardsPoint = view.findViewById(R.id.rewardsPointsTV);
        ImageView profileImage = view.findViewById(R.id.profileImageIV);
        //TextView userFirstName = view.findViewById(R.id.userFirstNameTV);
        SearchView searchView = view.findViewById(R.id.searchView);
        ImageButton sortingGuideButton = view.findViewById(R.id.sortingGuideIB);

        FrameLayout q1 = view.findViewById(R.id.quarter1Button);
        TextView q1Text = view.findViewById(R.id.quarter1Text);

        FrameLayout q2 = view.findViewById(R.id.quarter2Button);
        TextView q2Text = view.findViewById(R.id.quarter2Text);

        FrameLayout q3 = view.findViewById(R.id.quarter3Button);
        TextView q3Text = view.findViewById(R.id.quarter3Text);

        FrameLayout q4 = view.findViewById(R.id.quarter4Button);
        TextView q4Text = view.findViewById(R.id.quarter4Text);

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);

        Drawable buttonPressBackground = AppCompatResources.getDrawable(requireContext(), R.drawable.button_press_background);
        Drawable buttonBackground = AppCompatResources.getDrawable(requireContext(), R.drawable.button_background);

        int whiteColor = ContextCompat.getColor(requireContext(), R.color.white);
        int darkGreenColor = ContextCompat.getColor(requireContext(), R.color.dark_green);

        int currentYear;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentYear = LocalDate.now().getYear();
        } else {
            currentYear = 2024;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = LocalDate.now();
        }

        ArrayList<EventDay> greenDays = new ArrayList<>();

        greenDays.add(new EventDay(23, 1, 2024));
        greenDays.add(new EventDay(7, 3, 2024, 3));
        greenDays.add(new EventDay(14, 3, 2024, 2));
        greenDays.add(new EventDay(12, 3, 2024, 4));
        greenDays.add(new EventDay(12, 2, 2024, 5));

        setMonthView(greenDays);

        q1.setOnClickListener(v -> {
            q1.setBackground(buttonBackground);
            q2.setBackground(buttonPressBackground);
            q3.setBackground(buttonPressBackground);
            q4.setBackground(buttonPressBackground);

            q1Text.setTextColor(darkGreenColor);
            q2Text.setTextColor(whiteColor);
            q3Text.setTextColor(whiteColor);
            q4Text.setTextColor(whiteColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 1, 1);
            }
            setMonthView(greenDays);
        });

        q2.setOnClickListener(v -> {
            q1.setBackground(buttonPressBackground);
            q2.setBackground(buttonBackground);
            q3.setBackground(buttonPressBackground);
            q4.setBackground(buttonPressBackground);

            q1Text.setTextColor(whiteColor);
            q2Text.setTextColor(darkGreenColor);
            q3Text.setTextColor(whiteColor);
            q4Text.setTextColor(whiteColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 4, 1);
            }
            setMonthView(greenDays);
        });

        q3.setOnClickListener(v -> {
            q1.setBackground(buttonPressBackground);
            q2.setBackground(buttonPressBackground);
            q3.setBackground(buttonBackground);
            q4.setBackground(buttonPressBackground);

            q1Text.setTextColor(whiteColor);
            q2Text.setTextColor(whiteColor);
            q3Text.setTextColor(darkGreenColor);
            q4Text.setTextColor(whiteColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 7, 1);
            }
            setMonthView(greenDays);
        });

        q4.setOnClickListener(v -> {
            q1.setBackground(buttonPressBackground);
            q2.setBackground(buttonPressBackground);
            q3.setBackground(buttonPressBackground);
            q4.setBackground(buttonBackground);

            q1Text.setTextColor(whiteColor);
            q2Text.setTextColor(whiteColor);
            q3Text.setTextColor(whiteColor);
            q4Text.setTextColor(darkGreenColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 10, 1);
            }
            setMonthView(greenDays);
        });

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

        RecyclerView recyclerView = view.findViewById(R.id.myBadgesRV);

        ItemAdapter itemAdapter;
        ArrayList<Item> itemList = new ArrayList<>();

        int circleSelection = R.drawable.circle_selection;
        int circle = R.drawable.circle;
        int lightGreen = R.color.light_green;
        int darkGreen = R.color.dark_green;

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.enableNavigationViews(View.VISIBLE);

        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RewardsFragment rewardsFragment = new RewardsFragment();
                rewardsFragment.show(getParentFragmentManager(), rewardsFragment.getTag());
            }
        });

        weeklyStats.setOnClickListener(v -> {
            setBackgroundResources(weeklyStats, monthlyStats, yearlyStats, circleSelection, circle, circle);

            setColorFilters(weeklyStatsImage, monthlyStatsImage, yearlyStatsImage, lightGreen, darkGreen, darkGreen);
            setTextColors(weeklyStatsPoints, monthlyStatsPoints, yearlyStatsPoints, lightGreen, darkGreen, darkGreen);

            setTextValues("20", "Textile",
                    "16", "Aluminum",
                    "14", "Glass"
            );
        });

        monthlyStats.setOnClickListener(v -> {
            setBackgroundResources(weeklyStats, monthlyStats, yearlyStats, circle, circleSelection, circle);

            setColorFilters(weeklyStatsImage, monthlyStatsImage, yearlyStatsImage,  darkGreen, lightGreen, darkGreen);
            setTextColors(weeklyStatsPoints, monthlyStatsPoints, yearlyStatsPoints, darkGreen, lightGreen, darkGreen);

            setTextValues("80", "Cardboard",
                    "64", "Aluminum",
                    "56", "Glass"
            );

        });

        yearlyStats.setOnClickListener(v -> {
            setBackgroundResources(weeklyStats, monthlyStats, yearlyStats, circle, circle, circleSelection);

            setColorFilters(weeklyStatsImage, monthlyStatsImage, yearlyStatsImage,  darkGreen, darkGreen, lightGreen);
            setTextColors(weeklyStatsPoints, monthlyStatsPoints, yearlyStatsPoints, darkGreen, darkGreen, lightGreen);

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


    private void setMonthView(ArrayList<EventDay> greenDays) {
//        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<EventDay> daysInMonth = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Calculate the first day of the selected month
            LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);

            // Iterate over the previous 2 months and add their days to the list
            for (int i = -2; i <= 0; i++) {
                LocalDate month = firstOfMonth.plusMonths(i);
                int monthValue = month.getMonthValue();
                int yearValue = month.getYear();
                for (int day = 1; day <= month.lengthOfMonth(); day++) {
                    daysInMonth.add(new EventDay(day, monthValue, yearValue));
                }
            }
        }



        HeatmapAdapter calendarAdapter = new HeatmapAdapter(daysInMonth, greenDays, (position, dayText) -> {
            // Implement onItemClick method if needed
        });


        // Set up GridLayoutManager with horizontal orientation and 7 items per row
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 7, GridLayoutManager.HORIZONTAL, false);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

}