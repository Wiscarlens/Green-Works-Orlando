package com.orlando.greenworks.view.fragments;

import static com.orlando.greenworks.view.utils.DialogUtils.makeDialogFullscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orlando.greenworks.R;
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

public class ResetPasswordFragment extends BottomSheetDialogFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton closeButton = view.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        // Get the TextView and Button from the layout
        TextView resetNotAvailableErrorTextView = view.findViewById(R.id.reset_not_available_errorTextView);
        Button resetPasswordButton = view.findViewById(R.id.resetPasswordButton);

        // Set the OnClickListener for the button
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the button is clicked, make the TextView visible
                resetNotAvailableErrorTextView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        makeDialogFullscreen(dialog);

    }
}