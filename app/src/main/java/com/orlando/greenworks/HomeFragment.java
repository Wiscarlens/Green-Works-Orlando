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
import java.time.Month;
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

    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    ArrayList<Item> itemList = new ArrayList<>();
    ArrayList<EventDay> greenDays = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.enableNavigationViews(View.VISIBLE);

        MainActivity.fragmentTitle.setText(""); // Set the title of the fragment in the toolbar.

        LinearLayout rewardsLayout = view.findViewById(R.id.rewardsLayout);
        TextView rewardsPoint = view.findViewById(R.id.rewardsPointsTV);

        ImageView profileImage = view.findViewById(R.id.profileImageIV);

        TextView userFirstNameTV = view.findViewById(R.id.userFirstNameTV);

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

        RecyclerView recyclerView = view.findViewById(R.id.popularSearchRV);

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

        pressCurrentQuarterButton(q1, q2, q3, q4);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchHandler searchHandler = new SearchHandler();
                searchHandler.handleSearch(query, getParentFragmentManager());

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Implement if necessary for dynamic search/filtering
                return false;
            }
        });



        if (MainActivity.currentUser != null) {
            userFirstNameTV.setText(MainActivity.currentUser.getFirstName()); // Custom greeting for logged in users
        }

        UIController uiController = new UIController(requireActivity());

        rewardsLayout.setOnClickListener(view1 -> {
            if (MainActivity.currentUser != null) {
                uiController.openBottomSheet(new RewardsFragment());
            } else {
                uiController.changeFragment(new LoginFragment());
            }
        });

        profileImage.setOnClickListener(v -> {
            if (MainActivity.currentUser != null) {
                uiController.openBottomSheet(new ProfileFragment());
            } else {
                uiController.changeFragment(new LoginFragment());
            }
        });

        sortingGuideButton.setOnClickListener(v -> {
            SortingGuideFragment sortingGuideFragment = new SortingGuideFragment();
            sortingGuideFragment.show(getParentFragmentManager(), sortingGuideFragment.getTag());
        });

        // Heat map
        int currentYear;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentYear = LocalDate.now().getYear();
            selectedDate = LocalDate.now();
        } else {
            currentYear = 2024;
        }

        loadGreenDay();
        setMonthView(greenDays);

        // Progress Card
        Drawable buttonPressBackground = AppCompatResources.getDrawable(requireContext(), R.drawable.button_press_background);
        Drawable buttonBackground = AppCompatResources.getDrawable(requireContext(), R.drawable.button_background);

        int whiteColor = ContextCompat.getColor(requireContext(), R.color.white);
        int darkGreenColor = ContextCompat.getColor(requireContext(), R.color.dark_green);

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


        // Popular Search
        loadPopularItem();

        ItemAdapter itemAdapter = new ItemAdapter(itemList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);

        int circleSelection = R.drawable.circle_selection;
        int circle = R.drawable.circle;
        int lightGreen = R.color.light_green;
        int darkGreen = R.color.dark_green;

        // Set value for default stats
        setTextValues("20", "Textile",
                "16", "Aluminum",
                "14", "Glass"
        );

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void loadPopularItem() {
        itemList.add(
                new Item(
                        "dasani",
                        "Dasani",
                        "Recycling a Dasani water bottle begins with emptying any remaining liquid and removing the cap. Once cleaned, the bottle can be placed in a recycling bin designated for plastic materials. ",
                        "Most Dasani water bottles are made from PET (polyethylene terephthalate) plastic, which is commonly recyclable.",
                        7,
                        "January 10, 2024"
                )
        );

        itemList.add(
                new Item(
                        "cardboard",
                        "Cardboard",
                        "Recycling cardboard is a simple process that involves breaking down the material into smaller pieces and placing it in a recycling bin. Cardboard can be recycled multiple times and is a valuable material for creating new products.",
                        "Cardboard is a recyclable material that is commonly used for packaging and shipping products.",
                        13,
                        "January 9, 2024"
                )
        );

        itemList.add(
                new Item(
                        "spindrift",
                        "Spindrift",
                        "Recycling an aluminum can involves cleaning the can and placing it in a recycling bin designated for aluminum materials. Aluminum is a valuable material that can be recycled multiple times without losing its quality.",
                        "Aluminum is a recyclable material that is commonly used for beverage cans and food packaging.",
                        16,
                        "January 8, 2024"
                )
        );

        itemList.add(
                new Item(
                        "paper",
                        "Paper",
                        "Recycling paper involves breaking down the material into smaller pieces and placing it in a recycling bin. Paper can be recycled multiple times and is a valuable material for creating new products.",
                        "Paper is a recyclable material that is commonly used for printing and packaging products.",
                        9,
                        "April 27, 2024"
                )
        );

        itemList.add(
                new Item(
                        "glass",
                        "Glass",
                        "Recycling glass involves cleaning the material and placing it in a recycling bin designated for glass materials. Glass is a valuable material that can be recycled multiple times without losing its quality.",
                        "Glass is a recyclable material that is commonly used for beverage bottles and food containers.",
                        14,
                        "March 12, 2024"
                )
        );

        itemList.add(
                new Item(
                        "textile",
                        "Textile",
                        "Recycling textiles involves donating or repurposing old clothing and fabrics. Textiles can be recycled into new products or used for insulation and other applications.",
                        "Textiles are recyclable materials that include clothing, linens, and other fabric-based products.",
                        20,
                        "January 20, 2024"
                )
        );

        itemList.add(
                new Item(
                        "batteries",
                        "Batteries",
                        "Recycling batteries involves taking them to a designated recycling center or drop-off location. Batteries contain toxic materials that can harm the environment if not disposed of properly.",
                        "Batteries are recyclable materials that are commonly used to power electronic devices and vehicles.",
                        7,
                        "January 10, 2024"
                )
        );
    }

    private void setBackgroundResources(LinearLayout weeklyStats, LinearLayout monthlyStats,
                                        LinearLayout yearlyStats, int weeklyStatsBackground, int monthlyStatsBackground, int yearlyStatsBackground) {
        weeklyStats.setBackgroundResource(weeklyStatsBackground);
        monthlyStats.setBackgroundResource(monthlyStatsBackground);
        yearlyStats.setBackgroundResource(yearlyStatsBackground);
    }

    private void setColorFilters(ImageView weeklyStatsImage, ImageView monthlyStatsImage,
                                 ImageView yearlyStatsImage, int weeklyColor, int monthlyColor, int yearlyColor) {
        weeklyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), weeklyColor));
        monthlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), monthlyColor));
        yearlyStatsImage.setColorFilter(ContextCompat.getColor(requireContext(), yearlyColor));
    }

    private void setTextColors(@NonNull TextView weeklyStatsPoints, TextView monthlyStatsPoints,
                               TextView yearlyStatsPoints, int weeklyColor, int monthlyColor, int yearlyColor) {
        weeklyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), weeklyColor));
        monthlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), monthlyColor));
        yearlyStatsPoints.setTextColor(ContextCompat.getColor(requireContext(), yearlyColor));
    }

    private void setTextValues(String topOnePoints, String topOneMaterial, String topTwoPoints,
                               String topTwoMaterial, String topThreePoints, String topThreeMaterial) {
        statsTopOnePoints.setText(topOnePoints);
        statsTopOneMaterial.setText(topOneMaterial);

        statsTopTwoPoints.setText(topTwoPoints);
        statsTopTwoMaterial.setText(topTwoMaterial);

        statsTopThreePoints.setText(topThreePoints);
        statsTopThreeMaterial.setText(topThreeMaterial);
    }

    private void loadGreenDay() {
        greenDays.add(new EventDay(23, 1, 2024));
        greenDays.add(new EventDay(7, 3, 2024, 3));
        greenDays.add(new EventDay(14, 3, 2024, 2));
        greenDays.add(new EventDay(12, 3, 2024, 4));
        greenDays.add(new EventDay(12, 2, 2024, 5));
    }

    private void setMonthView(ArrayList<EventDay> greenDays) {
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

    public int getQuarterOfTheYear() {
        LocalDate currentDate;
        Month currentMonth;
        int currentMonthValue = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            currentMonth = currentDate.getMonth();
            currentMonthValue = currentMonth.getValue();
        }

        if (currentMonthValue <= 3) {
            return 1;
        } else if (currentMonthValue <= 6) {
            return 2;
        } else if (currentMonthValue <= 9) {
            return 3;
        } else {
            return 4;
        }
    }

    private void pressCurrentQuarterButton(FrameLayout q1, FrameLayout q2, FrameLayout q3, FrameLayout q4) {
        int currentQuarter = getQuarterOfTheYear();

        switch (currentQuarter) {
            case 1:
                q1.performClick();
                break;
            case 2:
                q2.performClick();
                break;
            case 3:
                q3.performClick();
                break;
            case 4:
                q4.performClick();
                break;
        }
    }

}