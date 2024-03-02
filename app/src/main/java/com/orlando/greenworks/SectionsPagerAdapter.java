package com.orlando.greenworks;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionsPagerAdapter extends FragmentStateAdapter {
    public SectionsPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        if (position == 0) {
            return new TrackFragment();
        } else {
            return new AchievementsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Since we have two tabs
    }
}


