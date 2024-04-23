package com.orlando.greenworks.view.fragments;

import static com.orlando.greenworks.view.utils.DialogUtils.makeDialogFullscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
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


public class ContactFragment extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton closeButton = view.findViewById(R.id.closeButton);

        TextInputEditText firstName = view.findViewById(R.id.firstNameText);
        TextInputEditText lastName = view.findViewById(R.id.lastNameText);
        TextInputEditText phoneNumber = view.findViewById(R.id.phoneNumberText);
        TextInputEditText email = view.findViewById(R.id.emailText);
        TextInputEditText message = view.findViewById(R.id.message);
        Button submitButton = view.findViewById(R.id.submitButton);

        closeButton.setOnClickListener(v -> dismiss());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This feature is not available yet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        makeDialogFullscreen(dialog);

    }
}