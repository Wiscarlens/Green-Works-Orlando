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
import android.database.Cursor;
import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private TextView loginErrorTextView;
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginErrorTextView = view.findViewById(R.id.login_errorTextView);
        loginErrorTextView.setVisibility(View.GONE);

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

        // Validate the user's email and password credentials and log them in
        db = new DatabaseHelper(getContext());
        loginButton.setOnClickListener(view1 -> {
            TextInputLayout emailLayout = view.findViewById(R.id.emailLayout);
            TextInputLayout passwordLayout = view.findViewById(R.id.passwordLayout);
            String email = emailLayout.getEditText().getText().toString();
            String password = passwordLayout.getEditText().getText().toString();
            // Hash the password
            String hashedPassword = null;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
                hashedPassword = Base64.encodeToString(hash, Base64.DEFAULT);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            // Check if the email and matching password exist in the database
            String[] columns = {"email_address", "password"};
            String selection = "email_address=? AND password=?";
            String[] selectionArgs = {email, hashedPassword};
            Cursor cursor = db.getReadableDatabase().query("User", columns, selection, selectionArgs, null, null, null);
            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            if (!exists) {
                loginErrorTextView.setVisibility(View.VISIBLE);
                return;
            }
            changeFragment(new HomeFragment());
        });


        // Continue as a guest
        TextView continueAsGuestLogin = view.findViewById(R.id.continueAsGuestLogin);

        continueAsGuestLogin.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.commit();
        });



        // Retrieve the message passed from the RegistrationFragment (account successfully created)
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