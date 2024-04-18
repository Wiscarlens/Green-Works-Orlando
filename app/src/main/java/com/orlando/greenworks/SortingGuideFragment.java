package com.orlando.greenworks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class SortingGuideFragment extends BottomSheetDialogFragment {

    //TODO: Add real data to Search History ("Most Frequent")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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