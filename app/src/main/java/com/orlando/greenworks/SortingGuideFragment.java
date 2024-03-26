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

public class SortingGuideFragment extends BottomSheetDialogFragment {


    //TODO: Add real data to "Most Frequent"
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

        itemAdapter = new ItemAdapter(itemList, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        recyclerView.setAdapter(itemAdapter);

        closeBtn.setOnClickListener(v -> dismiss());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SortingGuideFragment", "Search query submitted: " + query);

                // Check if an instance of ItemInformationFragment is already displayed
                ItemInformationFragment itemInformationFragment = (ItemInformationFragment) getParentFragmentManager().findFragmentByTag("ItemInformationFragment");

                // Update the search query of the ItemInformationFragment
                Bundle args = new Bundle();
                args.putString("searchQuery", query);

                if (itemInformationFragment != null && itemInformationFragment.isAdded()) {
                    // If it's already added, update the arguments and refresh the fragment
                    itemInformationFragment.setArguments(args);
                    getParentFragmentManager().beginTransaction().detach(itemInformationFragment).attach(itemInformationFragment).commit();
                } else {
                    // If not, create a new instance and show it
                    itemInformationFragment = new ItemInformationFragment();
                    itemInformationFragment.setArguments(args);
                    itemInformationFragment.show(getParentFragmentManager(), "ItemInformationFragment");
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