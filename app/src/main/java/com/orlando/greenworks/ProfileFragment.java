package com.orlando.greenworks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

public class ProfileFragment extends BottomSheetDialogFragment {

    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

        db = new DatabaseHelper(getContext()); // Initialize the DatabaseHelper

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

        TextView profileName = view.findViewById(R.id.profileName);
        TextView profilePhoneNumber = view.findViewById(R.id.phone_number_change_textView); // Updated ID
        TextView profileAddress = view.findViewById(R.id.address_change_textView); // Updated ID
        TextView profileEmail = view.findViewById(R.id.email_change_textView); // Updated ID

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String email = sharedPreferences.getString("email", "");
            String[] columns = {"first_name", "last_name", "phone_number", "address", "email_address"}; // Added phone_number, address, and email_address
            String selection = "email_address=?";
            String[] selectionArgs = {email};
            Cursor cursor = db.getReadableDatabase().query("User", columns, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                int firstNameIndex = cursor.getColumnIndex("first_name");
                int lastNameIndex = cursor.getColumnIndex("last_name");
                int phoneNumberIndex = cursor.getColumnIndex("phone_number"); // Added
                int addressIndex = cursor.getColumnIndex("address"); // Added
                int emailAddressIndex = cursor.getColumnIndex("email_address"); // Added

                if (firstNameIndex != -1 && lastNameIndex != -1 && phoneNumberIndex != -1 && addressIndex != -1 && emailAddressIndex != -1) {
                    String firstName = cursor.getString(firstNameIndex);
                    String lastName = cursor.getString(lastNameIndex);
                    String phoneNumber = cursor.getString(phoneNumberIndex); // Added
                    String address = cursor.getString(addressIndex); // Added
                    String emailAddress = cursor.getString(emailAddressIndex); // Added

                    profileName.setText(firstName + " " + lastName);
                    profilePhoneNumber.setText(phoneNumber); // Added
                    profileAddress.setText(address); // Added
                    profileEmail.setText(emailAddress); // Added
                }
            }
            cursor.close();
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