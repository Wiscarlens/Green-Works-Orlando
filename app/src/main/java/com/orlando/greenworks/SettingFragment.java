package com.orlando.greenworks;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

/**
 * The SettingFragment class extends the BottomSheetDialogFragment class.
 * It represents a settings dialog that slides up from the bottom of the screen.
 * The settings dialog includes options for the user to change the app theme, enable notifications, and change the app language.
 */
public class SettingFragment extends BottomSheetDialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * This method is called when the fragment is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    /**
     * This method is called to have the fragment instantiate its user interface view.
     * It inflates the layout for this fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());

        TextView about = view.findViewById(R.id.about);
        TextView version = view.findViewById(R.id.version);

        // Get the app version
        String versionName;
        try {
            versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "N/A";
        }
        version.setText("App Version: " + versionName);

        about.setOnClickListener(v -> {
            if (version.getVisibility() == View.GONE) {
                version.setVisibility(View.VISIBLE);
            } else {
                version.setVisibility(View.GONE);
            }
        });

        // Add the click listener for the 'Theme' TextView
        TextView themeSettings = view.findViewById(R.id.theme_settings);
        LinearLayout themeOptions = view.findViewById(R.id.theme_options);

        themeSettings.setOnClickListener(v -> {
            if (themeOptions.getVisibility() == View.GONE) {
                themeOptions.setVisibility(View.VISIBLE);
            } else {
                themeOptions.setVisibility(View.GONE);
            }
        });

        // Add the change listener for the 'Change to Dark Mode' Switch
        Switch darkModeSwitch = view.findViewById(R.id.dark_mode_switch);
        TextView darkModeStatus = view.findViewById(R.id.dark_mode_status);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                darkModeStatus.setVisibility(View.VISIBLE);
            } else {
                darkModeStatus.setVisibility(View.GONE);
            }
        });

        // Add the click listener for the 'Notification' TextView
        TextView notificationSettings = view.findViewById(R.id.notification_settings);
        Button notificationStatus = view.findViewById(R.id.notification_status);

        // Initially hide the 'Enable Notifications' button
        notificationStatus.setVisibility(View.GONE);

        notificationSettings.setOnClickListener(v -> {
            // Toggle the visibility of the 'Enable Notifications' button when 'Notifications' text box is clicked
            if (notificationStatus.getVisibility() == View.GONE) {
                notificationStatus.setVisibility(View.VISIBLE);
            } else {
                notificationStatus.setVisibility(View.GONE);
            }
        });

        notificationStatus.setOnClickListener(v -> {

            // Code to enable notifications goes here
            // For now, just change the button text

            NotificationHelper.createNotificationChannel(getContext());
            NotificationHelper.displayNotification(getContext(), "Trash Pickup Day", "Your next trash pickup day is May 15th");
            notificationStatus.setText("Notifications Enabled");
            Toast.makeText(getActivity(), "You have enabled notifications", Toast.LENGTH_SHORT).show();
            // Add the code to open the system settings for the app
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

            //for Android 5-7
            intent.putExtra("app_package", getContext().getPackageName());
            intent.putExtra("app_uid", getContext().getApplicationInfo().uid);

            // for Android 8 and above
            intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getPackageName());

            startActivity(intent);
        });

        // Add the click listener for 'Language' TextView
        TextView languageSettings = view.findViewById(R.id.language_settings);
        TextView languageStatus = view.findViewById(R.id.language_status);

        languageSettings.setOnClickListener(v -> {
            if (languageStatus.getVisibility() == View.GONE) {
                languageStatus.setVisibility(View.VISIBLE);
            } else {
                languageStatus.setVisibility(View.GONE);
            }
        });


    }

    /**
     * This method is called when the fragment is visible to the user and actively running.
     * It sets the state of the bottom sheet dialog to expanded.
     */
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