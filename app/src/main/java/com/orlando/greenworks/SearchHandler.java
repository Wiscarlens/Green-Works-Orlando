package com.orlando.greenworks;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class SearchHandler {
    private boolean isSearchInProgress = false;

    public void handleSearch(String query, FragmentManager fragmentManager) {
        Log.d("SortingGuideFragment", "Search query submitted: " + query);

        // If a search is already in progress, ignore this search request
        if (isSearchInProgress) {
            return;
        }
        // Set the flag to indicate that a search is in progress
        isSearchInProgress = true;

        // Check if an instance of ItemInformationFragment is already displayed
        ItemInformationFragment itemInformationFragment = (ItemInformationFragment) fragmentManager.findFragmentByTag("ItemInformationFragment");

        // Update the search query of the ItemInformationFragment
        Bundle args = new Bundle();
        args.putString("searchQuery", query);

        if (itemInformationFragment != null && itemInformationFragment.isAdded()) {
            // If it's already added, replace the fragment with a new one with updated arguments
            itemInformationFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, itemInformationFragment, "ItemInformationFragment").commit();
        } else {
            // If not, create a new instance and show it
            itemInformationFragment = new ItemInformationFragment();
            itemInformationFragment.setArguments(args);
            itemInformationFragment.show(fragmentManager, "ItemInformationFragment");
        }

        // Re-enable the search button after a delay to prevent multiple rapid searches and multiple instances of ItemInformationFragment
        new Handler().postDelayed(() -> isSearchInProgress = false, 1000);
    }
}
