package com.orlando.greenworks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SortingGuideFragment extends BottomSheetDialogFragment {


    //TODO: Add real data to "Most Frequent"
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting_guide, container, false);

        SearchView searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SortingGuideFragment", "Search query submitted: " + query);

                // Create a new instance of ItemInformationFragment
                ItemInformationFragment itemInformationFragment = new ItemInformationFragment();

                // Bundle to pass the search query to ItemInformationFragment
                Bundle args = new Bundle();
                args.putString("searchQuery", query); // Use the same key when retrieving the value in ItemInformationFragment
                itemInformationFragment.setArguments(args);

                // Replace SortingGuideFragment with ItemInformationFragment in the fragment container
                if (getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, itemInformationFragment);
                    transaction.addToBackStack(null); // Optional, allows users to navigate back to the previous fragment
                    transaction.commit();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Implement if necessary for dynamic search/filtering
                return false;
            }
        });

        return view;
    }
}
