package com.orlando.greenworks;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private Item item;

    private static final String ARG_ITEM = "item";

    private static boolean isDataFetching = true;

    public ItemInformationFragment() {

    }


    public static ItemInformationFragment newInstance(Item item, boolean isDataFetching) {
        ItemInformationFragment fragment = new ItemInformationFragment();

        ItemInformationFragment.isDataFetching = isDataFetching;

        // Update the Item object in the newInstance method
        fragment.item = item;

        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            item = (Item) getArguments().getSerializable(ARG_ITEM);
        }

        if (isDataFetching){
            // Handle the argument passed to this fragment
            Bundle arguments = getArguments();

            if (arguments != null && arguments.containsKey("searchQuery")) {
                searchQuery = arguments.getString("searchQuery"); // Store the search query
                FetchItems fetchItems = new FetchItems(this);
                fetchItems.fetchItems(searchQuery);
            }
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

        closeBtn.setOnClickListener(v -> dismiss());

        disposeButton.setOnClickListener(v -> {
            dismiss();
            Toast.makeText(requireContext(), "Dispose", Toast.LENGTH_SHORT).show();
        });

        exitButton.setOnClickListener(v -> dismiss());



        if (itemDetailsToDisplay != null) {
            displayItemInformation(itemDetailsToDisplay);

            disposeButton.setVisibility(View.VISIBLE);
        } else {
            if (!isDataFetching && item != null) {
                String imageName = item.getItemImagePath();
                int imageResId = requireContext().getResources().getIdentifier(imageName, "drawable", requireContext().getPackageName());

                itemImage.setImageResource(imageResId);
                itemName.setText(item.getItemName());
                itemRecyclingInfo.setText(item.getItemMaterial());
                itemInformation.setText(item.getItemDescription());

                disposeButton.setVisibility(View.GONE);

                isDataFetching = true;
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Add a delay of 2 seconds before setting the visibility of ui elements
        new Handler().postDelayed(() -> {
            if (itemName != null) {
                scrollView.setVisibility(View.VISIBLE);
                loaderAnimation.setVisibility(View.GONE);
            }
        }, 2000); // Delay of 2 seconds to let api call finish



    }

    public void displayItemInformation(JSONObject itemDetails) {
        try {
            updateItemDetails(itemDetails);
            updateItemInformationViews();
        } catch (JSONException e) {
            Log.d("Item Information Fragment", e.toString());

            String message = getString(R.string.no_items_found);

            itemInformation.setText(message);
        }
    }

    private void updateItemDetails(JSONObject itemDetails) throws JSONException {
        String name = itemDetails.optString("name", "");
        String description = itemDetails.optString("description", "No description available");
        String tagInfo = extractTagInfo(itemDetails.optJSONArray("tags"));

        Log.i("ItemInformationFragment", "updateItemDetails: " + name + " " + description + " " + tagInfo);

        this.item = new Item();

        item.setItemName(name.isEmpty() ? "" : name);
        item.setItemDescription(description.isEmpty() ? "No description available\n" : description + "\n");
        item.setItemMaterial(tagInfo);
    }

    private String extractTagInfo(JSONArray tags) throws JSONException {
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
        return tagInfo.toString().trim();
    }

//    private void updateItemInformationViews() {
//        itemName.setText(item.getItemName());
//        itemInformation.setText(item.getItemDescription());
//        itemRecyclingInfo.setText(item.getItemMaterial());
//
//        View view = getView();
//        if (view != null) {
//            int imageResId = getImageResIdBasedOnItemName(item.getItemName());
//            itemImage.setImageResource(imageResId);
//        }
//
//        disposeButton.setVisibility(View.VISIBLE);
//    }

    private void updateItemInformationViews() {
        if (itemName != null) {
            itemName.setText(item.getItemName());
        }
        if (itemInformation != null) {
            itemInformation.setText(item.getItemDescription());
        }
        if (itemRecyclingInfo != null) {
            itemRecyclingInfo.setText(item.getItemMaterial());
        }

        View view = getView();
        if (view != null) {
            int imageResId = getImageResIdBasedOnItemName(item.getItemName());
            itemImage.setImageResource(imageResId);
        }

        disposeButton.setVisibility(View.VISIBLE);
    }


    private int getImageResIdBasedOnItemName(String itemName) {
        switch (itemName) {
            case "Soda Can":
                return R.drawable.item_id_1_soda_can;
            case "Plastic Grocery Bag":
                return R.drawable.item_id_2_plastic_bag;
            case "Laptop":
                return R.drawable.item_id_3_laptop;
            case "Desktop Computer":
                return R.drawable.item_id_4_desktop_computer;
            case "Face Mask":
                return R.drawable.item_id_5_face_mask;
            case "Food":
                return R.drawable.item_id_6_food;
            case "Broken Glass":
                return R.drawable.item_id_7_broken_glass;
            case "Pet Food Bags":
                return R.drawable.item_id_8_pet_food_bags;
            case "Clothes":
                return R.drawable.item_id_9_clothes;
            case "Paper":
                return R.drawable.item_id_10_paper;
            case "Cardboard Box":
                return R.drawable.item_id_11_cardboard_box;
            case "Disposable Battery":
                return R.drawable.item_id_12_disposable_battery;
            default:
                return R.drawable.not_found;
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
