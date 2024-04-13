package com.orlando.greenworks;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UIController {
    private final FragmentActivity activity;

    public UIController(FragmentActivity activity) {
        this.activity = activity;
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void openBottomSheet(DialogFragment fragment) {
        fragment.show(activity.getSupportFragmentManager(), fragment.getTag());
    }

}
