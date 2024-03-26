package com.orlando.greenworks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Handler;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class ItemInformationFragment extends BottomSheetDialogFragment {

    private TextView itemInformationTextView;

    private ImageView itemImage;
    private JSONObject itemDetailsToDisplay;
    private String searchQuery; // Added to store the search query

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle the argument passed to this fragment
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("searchQuery")) {
            searchQuery = arguments.getString("searchQuery"); // Store the search query
            FetchItems fetchItems = new FetchItems(this);
            fetchItems.fetchItems(searchQuery);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_information, container, false);

        ImageButton closeBtn = view.findViewById(R.id.closeButton);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the bottom sheet
                dismiss();
            }
        });

        itemInformationTextView = view.findViewById(R.id.item_information);
        itemImage = view.findViewById(R.id.item_image);


        // Initially set the visibility to invisible
        itemInformationTextView.setVisibility(View.INVISIBLE);
        itemImage.setVisibility(View.INVISIBLE);

        Button btnReturnToSortingGuide = view.findViewById(R.id.btnReturnToSortingGuide);

        btnReturnToSortingGuide.setOnClickListener(v -> {
            SortingGuideFragment sortingGuideFragment = new SortingGuideFragment();
            sortingGuideFragment.show(getParentFragmentManager(), sortingGuideFragment.getTag());
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Add a delay of 2 seconds before setting the visibility of ui elements
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (itemInformationTextView != null && itemImage != null) {
                    itemInformationTextView.setVisibility(View.VISIBLE);
                    itemImage.setVisibility(View.VISIBLE);
                }
            }
        }, 2000); // Delay of 2 seconds to let api call finish



        if (itemDetailsToDisplay != null) {
            displayItemInformation(itemDetailsToDisplay);
        } else {
            // Display custom message when no item details are available
            itemInformationTextView.setText("No items found for \"" + searchQuery + "\".");
        }
    }

    public void receiveItemInformation(JSONObject itemDetails) {
        itemDetailsToDisplay = itemDetails;
        if (isVisible()) {
            displayItemInformation(itemDetailsToDisplay);
        } else {
            // This else part is unlikely to be needed but added for consistency
            itemInformationTextView.setText("No items found for '" + searchQuery + "'.");
        }
    }

    public void displayItemInformation(JSONObject itemDetails) {
        Log.d("ItemInformationFragment", "Displaying item information: " + itemDetails.toString());
        Log.d("ItemInformationFragment", "Is attached to activity: " + (getActivity() != null));
        Log.d("ItemInformationFragment", "Is visible: " + isVisible());
        try {
            if (itemDetails == null) {
                itemInformationTextView.setText("No items found for '" + searchQuery + "'.");
                return;
            }

            String name = itemDetails.optString("name", "");
            String description = itemDetails.optString("description", "No description available");

            // Extract and format recycling information
            JSONArray tags = itemDetails.optJSONArray("tags");
            StringBuilder tagInfo = new StringBuilder();

            if (tags != null) {
                for (int j = 0; j < tags.length(); j++) {
                    JSONObject tag = tags.getJSONObject(j);
                    String tagName = tag.optString("name", "");
                    String tagDescription = tag.optString("description", "");

                    if (!tagName.isEmpty()) {
                        tagInfo.append(tagName);
                        if (!tagDescription.isEmpty()) {
                            tagInfo.append(" - ").append(tagDescription);
                        }
                        tagInfo.append("\n");
                    }
                }
            }

            String itemInformation = (name.isEmpty() ? "" : name + "\n") +
                    (description.isEmpty() ? "No description available\n" : description + "\n") +
                    tagInfo.toString().trim();
            itemInformationTextView.setText(itemInformation);



            // ADDED CODE TO DISPLAY IMAGE BASED ON API ITEM NAME
            View view = getView();
            if (view != null) {
                ImageView itemImage = view.findViewById(R.id.item_image);
                String itemName = itemDetails.optString("name", "");

                if ("Soda Can".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_01_soda_can);
                } else if ("Plastic Grocery Bag".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_02_plastic_bag);
                } else if ("Laptop".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_03_laptop);
                } else if ("Desktop Computer".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_04_desktop_computer);
                } else if ("Face Mask".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_05_face_mask);
                } else if ("Food".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_06_food);
                } else if ("Broken Glass".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_07_broken_glass);
                } else if ("Pet Food Bags".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_08_pet_food_bags);
                } else if ("Clothes".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_09_clothes);
                } else if ("Paper".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_10_paper);
                } else if ("Cardboard Box".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_11_cardboard_box);
                } else {
                    itemImage.setImageResource(R.drawable.no_image_available);
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
            itemInformationTextView.setText("No items found for '" + searchQuery + "'.");
        }
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
