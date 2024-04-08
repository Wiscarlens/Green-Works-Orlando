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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

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


        TextView continueAsGuestLogin = view.findViewById(R.id.continueAsGuestLogin);

        continueAsGuestLogin.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.commit();
        });




        // Retrieve the message passed from the RegistrationFragment account successfully created
        Bundle bundle = getArguments();
        if (bundle != null) {
            String message = bundle.getString("message");

            // Display the message as a Toast
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
           }
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Snackbar account success message
        // Get the message from the arguments
        //String message = getArguments() != null ? getArguments().getString("message") : null;

        // Display the message as a Snackbar with indefinite duration
        //if (message != null) {
           // Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show();
        //}


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