package com.orlando.greenworks;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;


public class RewardsFragment extends BottomSheetDialogFragment {
    /*
     * This is a collaborative effort by the following team members:
     * Team members:
     * - Wiscarlens Lucius (Team Leader)
     * - Amanpreet Singh
     * - Alexandra Perez
     * - Eric Klausner
     * - Jordan Kinlocke
     * */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        ArrayList<Badge> badgeList = new ArrayList<>();
        ArrayList<Badge> badgeProgressList = new ArrayList<>();

        RecyclerView badgeRecyclerView = view.findViewById(R.id.myBadgesRV);
        RecyclerView badgeProgressRecyclerView = view.findViewById(R.id.myBadgesProgressRV);

        BadgeAdapter badgeAdapter;
        BadgeProgressAdapter badgeProgressAdapter;

        ImageButton closeButton = view.findViewById(R.id.closeButton);


        badgeList.add(new Badge("Recycling Badge", "badge1"));
        badgeList.add(new Badge("Composting Badge", "badge2"));
        badgeList.add(new Badge("Water Conservation", "badge3"));
        badgeList.add(new Badge("Energy Conservation", "badge1"));

        badgeAdapter = new BadgeAdapter(badgeList, getContext());
        badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        badgeRecyclerView.setAdapter(badgeAdapter);

        badgeProgressList.add(new Badge("Recycling", "badge1", 100, 50));
        badgeProgressList.add(new Badge("Composting", "badge2", 100, 75));
        badgeProgressList.add(new Badge("Water Conservation", "badge3", 100, 25));
        badgeProgressList.add(new Badge("Energy Conservation", "badge1", 100, 100));

        badgeProgressAdapter = new BadgeProgressAdapter(badgeProgressList, getContext());
        badgeProgressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        badgeProgressRecyclerView.setAdapter(badgeProgressAdapter);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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