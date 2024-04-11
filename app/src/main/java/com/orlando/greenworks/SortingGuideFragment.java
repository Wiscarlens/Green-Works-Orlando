package com.orlando.greenworks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.Objects;
import android.os.Handler; // ADDED to fix multiple instances issue in SortingGuideFragment and ItemInformationFragment

public class SortingGuideFragment extends BottomSheetDialogFragment {


    private boolean isSearchInProgress = false; // ADDED to fix multiple instances issue in SortingGuideFragment and ItemInformationFragment


    //TODO: Add real data to Search History ("Most Frequent")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting_guide, container, false);

        ImageButton closeBtn = view.findViewById(R.id.closeButton);

        SearchView searchView = view.findViewById(R.id.search);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        int numberOfColumns = 3;


        ItemAdapter itemAdapter;
        ArrayList<Item> itemList = new ArrayList<>();

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



        itemAdapter = new ItemAdapter(itemList, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        recyclerView.setAdapter(itemAdapter);

        closeBtn.setOnClickListener(v -> dismiss());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SortingGuideFragment", "Search query submitted: " + query);

                // ADDED to fix multiple instances issue in SortingGuideFragment and ItemInformationFragment
                // If a search is already in progress, ignore this search request
                if (isSearchInProgress) {
                    return true;
                }
                // Set the flag to indicate that a search is in progress
                isSearchInProgress = true;

                // Check if an instance of ItemInformationFragment is already displayed
                ItemInformationFragment itemInformationFragment = (ItemInformationFragment) getParentFragmentManager().findFragmentByTag("ItemInformationFragment");

                // Update the search query of the ItemInformationFragment
                Bundle args = new Bundle();
                args.putString("searchQuery", query);

                if (itemInformationFragment != null && itemInformationFragment.isAdded()) {
                    // If it's already added, replace the fragment with a new one with updated arguments
                    //itemInformationFragment = new ItemInformationFragment(); REMOVE THIS LINE to fix multiple instances issue in SortingGuideFragment and ItemInformationFragment
                    itemInformationFragment.setArguments(args);
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, itemInformationFragment, "ItemInformationFragment").commit();
                } else {
                    // If not, create a new instance and show it
                    itemInformationFragment = new ItemInformationFragment();
                    itemInformationFragment.setArguments(args);
                    itemInformationFragment.show(getParentFragmentManager(), "ItemInformationFragment");
                }

                // Re-enable the search button after a delay to prevent multiple rapid searches and multiple instances of ItemInformationFragment
                new Handler().postDelayed(() -> isSearchInProgress = false, 1000);

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

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();

        if (dialog != null) {
            ViewGroup bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
                behavior.setHideable(true);

                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}