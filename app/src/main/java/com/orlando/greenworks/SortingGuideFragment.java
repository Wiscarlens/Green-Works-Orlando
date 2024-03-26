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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.Objects;
import android.widget.ImageButton;


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

                // Dismiss the SortingGuideFragment when user enters search query. This will
                // remove the bottom sheet from the screen so the user can see item information screen.
                // Added since bottom sheet layout is now fullscreen and search query results were not obvious to user.
                dismiss();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Make close button work
        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // This will dismiss the BottomSheetDialogFragment
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

}}}