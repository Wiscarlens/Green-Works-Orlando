package com.orlando.greenworks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;
import android.os.Handler;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class ItemInformationFragment extends BottomSheetDialogFragment {

    private ScrollView scrollView;
    private ImageView itemImage;
    private TextView itemName;
    private TextView itemInformation;
    private TextView itemRecyclingInfo;
    private Button disposeButton;
    private LottieAnimationView loaderAnimation;
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
        loaderAnimation = view.findViewById(R.id.loaderAnimation);
        scrollView = view.findViewById(R.id.item_information_scroll_view);
        itemImage = view.findViewById(R.id.item_image);
        itemName = view.findViewById(R.id.item_name);
        itemInformation = view.findViewById(R.id.item_details);
        itemRecyclingInfo = view.findViewById(R.id.itemRecyclingInfo);
        disposeButton = view.findViewById(R.id.disposeButton);
        Button exitButton = view.findViewById(R.id.btnReturnToSortingGuide);

        scrollView.setVisibility(View.INVISIBLE);
        loaderAnimation.setVisibility(View.VISIBLE);
        disposeButton.setVisibility(View.VISIBLE);

        closeBtn.setOnClickListener(v -> {
            dismiss();
        });

        disposeButton.setOnClickListener(v -> {
            dismiss();
            Toast.makeText(requireContext(), "Dispose", Toast.LENGTH_SHORT).show();
        });

        exitButton.setOnClickListener(v -> {
            dismiss();
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
                if (itemInformation != null && itemImage != null) {
                    scrollView.setVisibility(View.VISIBLE);
                    loaderAnimation.setVisibility(View.GONE);
                }
            }
        }, 2000); // Delay of 2 seconds to let api call finish


        if (itemDetailsToDisplay != null) {
            displayItemInformation(itemDetailsToDisplay);
            disposeButton.setVisibility(View.VISIBLE);
        } else {
            disposeButton.setVisibility(View.GONE);

            itemImage.setImageResource(R.drawable.not_found);

            // Display custom message when no item details are available
            String message = "No items found for \"" + searchQuery + "\".";
            itemName.setText("");
            itemRecyclingInfo.setText("");
            itemInformation.setText(message);
        }
    }

    @Deprecated
    public void receiveItemInformation(JSONObject itemDetails) {
        itemDetailsToDisplay = itemDetails;
        if (isVisible()) {
            displayItemInformation(itemDetailsToDisplay);
        } else {

            // This else part is unlikely to be needed but added for consistency
            itemInformation.setText("No items found for '" + searchQuery + "'.");
        }
    }

    public void displayItemInformation(JSONObject itemDetails) {
        Log.d("ItemInformationFragment", "Displaying item information: " + itemDetails.toString());
        Log.d("ItemInformationFragment", "Is attached to activity: " + (getActivity() != null));
        Log.d("ItemInformationFragment", "Is visible: " + isVisible());

        try {
            if (itemDetails == null) {
                itemInformation.setText("No items found for '" + searchQuery + "'.");
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

            String itemNameString = (name.isEmpty() ? "" : name);
            String itemInformationString = (description.isEmpty() ? "No description available\n" : description + "\n");
            String itemRecyclingInfoString = tagInfo.toString().trim();

            itemName.setText(itemNameString);
            itemInformation.setText(itemInformationString);
            itemRecyclingInfo.setText(itemRecyclingInfoString);

            disposeButton.setVisibility(View.VISIBLE);

            // ADDED CODE TO DISPLAY IMAGE BASED ON API ITEM NAME
            View view = getView();

            if (view != null) {
                ImageView itemImage = view.findViewById(R.id.item_image);
                String itemName = itemDetails.optString("name", "");

                if ("Soda Can".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_1_soda_can);
                } else if ("Plastic Grocery Bag".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_2_plastic_bag);
                } else if ("Laptop".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_3_laptop);
                } else if ("Desktop Computer".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_4_desktop_computer);
                } else if ("Face Mask".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_5_face_mask);
                } else if ("Food".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_6_food);
                } else if ("Broken Glass".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_7_broken_glass);
                } else if ("Pet Food Bags".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_8_pet_food_bags);
                } else if ("Clothes".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_9_clothes);
                } else if ("Paper".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_10_paper);
                } else if ("Cardboard Box".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_11_cardboard_box);
                } else if ("Disposable Battery".equals(itemName)) {
                    itemImage.setImageResource(R.drawable.item_id_12_disposable_battery);
                } else {
                    itemImage.setImageResource(R.drawable.not_found);
                }
            }



        } catch (JSONException e) {
            Log.d("Item Information Fragment", e.toString());

            itemInformation.setText("No items found for '" + searchQuery + "'.");
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
