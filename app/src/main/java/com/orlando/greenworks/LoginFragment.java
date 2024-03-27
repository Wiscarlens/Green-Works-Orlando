package com.orlando.greenworks;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    EditText logEmail, logPassword;
    Button loginButton;
    LocalDataStorage localDataStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.enableNavigationViews(View.GONE);

        TextView forgotPassword = view.findViewById(R.id.forgotPassword);
        LinearLayout signUp = view.findViewById(R.id.signUpLoginLL);
        Button loginButton = view.findViewById(R.id.loginButton);

        forgotPassword.setOnClickListener(view1 -> {
            ResetPasswordFragment resetPassword = new ResetPasswordFragment();
            resetPassword.show(getParentFragmentManager(), resetPassword.getTag());
        });

        signUp.setOnClickListener(view1 -> {
            RegistrationFragment registrationFragment = new RegistrationFragment();
            registrationFragment.show(getParentFragmentManager(), registrationFragment.getTag());
        });

        loginButton.setOnClickListener(view1 -> {
            changeFragment(new HomeFragment());
        });

        logEmail = logEmail.findViewById(R.id.logEmail);
        logPassword = logPassword.findViewById(R.id.logPassword);
        loginButton = loginButton.findViewById(R.id.loginButton);

        localDataStorage = new LocalDataStorage(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUsername = logEmail.getText().toString().trim();
                String loginPassword = logPassword.getText().toString().trim();

                // Retrieve user data from local storage
                String[] userData = localDataStorage.getUserData();
                String savedEmail = userData[3];
                String savedPassword = userData[4];

                if (loginUsername.equals(savedEmail) && loginPassword.equals(savedPassword)) {
                    // Login successful
//                    Toast.makeText(LoginFragment.this, "Login successful", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(LoginFragment.this, HomeFragment.class);
//                    startActivity(intent);

                    // Finish current activity to prevent user from coming back to login screen using back button
//                    mainActivity.finish();
                } else {
                    // Login failed
//                    Toast.makeText(LoginFragment.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}