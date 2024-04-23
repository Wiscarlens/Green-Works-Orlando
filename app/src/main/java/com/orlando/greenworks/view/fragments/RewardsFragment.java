package com.orlando.greenworks.view.fragments;

import static com.orlando.greenworks.view.utils.DialogUtils.makeDialogFullscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orlando.greenworks.R;
import com.orlando.greenworks.model.Badge;
import com.orlando.greenworks.view.adapter.BadgeAdapter;
import com.orlando.greenworks.view.adapter.BadgeProgressAdapter;
import com.orlando.greenworks.view.utils.NotificationHelper;

import java.util.ArrayList;


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


        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        makeDialogFullscreen(dialog);

    }
}