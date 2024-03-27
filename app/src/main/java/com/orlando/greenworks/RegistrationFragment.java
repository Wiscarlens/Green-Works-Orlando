package com.orlando.greenworks;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.view.LayoutInflater;
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


public class RegistrationFragment extends BottomSheetDialogFragment {

    EditText regFirstName, regLastName, regAddress, regEmail, regPassword, regConfirmPassword;
    Button regAccountBtn;
    LocalDataStorage localDataStorage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        TextView termsAndConditions = view.findViewById(R.id.termsConditions);
        TextView login = view.findViewById(R.id.loginLink);

        login.setOnClickListener(v -> dismiss());

        termsAndConditions.setOnClickListener(v -> {
            TermsFragment termsFragment = new TermsFragment();
            termsFragment.show(getChildFragmentManager(), termsFragment.getTag());
        });

        closeButton.setOnClickListener(v -> dismiss());


        // Inflate the layout for this fragment

        regFirstName= regFirstName.findViewById(R.id.regFirstName);
        regLastName= regLastName.findViewById(R.id.regLastName);
        regAddress = regAddress.findViewById(R.id.regAddress);
        regEmail = regEmail.findViewById(R.id.regEmail);
        regPassword = regPassword.findViewById(R.id.logPassword);
        regConfirmPassword = regConfirmPassword.findViewById(R.id.regConfirmPassword);
        localDataStorage = new LocalDataStorage(this);

        regAccountBtn = regAccountBtn.findViewById(R.id.regAccountBtn);

        regAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = regFirstName.getText().toString().trim();
                String lastName = regLastName.getText().toString().trim();
                String address = regAddress.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String password = regPassword.getText().toString().trim();
                String confirmPassword = regConfirmPassword.getText().toString().trim();

                localDataStorage.saveUserData(firstName,lastName,address,email,password,confirmPassword);

//                Toast.makeText(new RegistrationFragment(), "Signup successful", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

    public SharedPreferences getSharedPreferences(String prefName, int modePrivate) {
        return null;
    }
}