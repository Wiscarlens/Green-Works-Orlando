package com.orlando.orlandorecycle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

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