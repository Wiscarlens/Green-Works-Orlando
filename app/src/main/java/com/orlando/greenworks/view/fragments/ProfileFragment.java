package com.orlando.greenworks.view.fragments;

import static com.orlando.greenworks.view.utils.DialogUtils.makeDialogFullscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orlando.greenworks.R;
import com.orlando.greenworks.view.MainActivity;
import com.orlando.greenworks.view.utils.NotificationHelper;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class ProfileFragment extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

//        DatabaseHelper db = new DatabaseHelper(getContext()); // Initialize the DatabaseHelper

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());

        ImageView profilePictureIV = view.findViewById(R.id.profileImageIV);
        TextView fullNameTV = view.findViewById(R.id.profileName);
        TextView phoneNumberTV = view.findViewById(R.id.phone_number_change_textView); // Updated ID
        TextView addressTV = view.findViewById(R.id.address_change_textView); // Updated ID
        TextView emailTV = view.findViewById(R.id.email_change_textView); // Updated ID

        if (MainActivity.currentUser != null) {
            String fullName = MainActivity.currentUser.getFirstName() + " " + MainActivity.currentUser.getLastName();
            fullNameTV.setText(fullName);
            phoneNumberTV.setText(MainActivity.currentUser.getPhoneNumber());
            addressTV.setText(MainActivity.currentUser.getAddress());
            emailTV.setText(MainActivity.currentUser.getEmail());
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        makeDialogFullscreen(dialog);

    }
}