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

import android.util.Patterns;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.Context;

public class LoginFragment extends Fragment {

//    private TextView loginErrorTextView;
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

//        loginErrorTextView = view.findViewById(R.id.login_errorTextView);
//        loginErrorTextView.setVisibility(View.GONE);

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.enableNavigationViews(View.GONE);

        TextInputEditText emailText = view.findViewById(R.id.emailText);
        TextInputLayout emailLayout = view.findViewById(R.id.emailLayout);
        TextInputEditText passwordText = view.findViewById(R.id.passwordText);
        TextInputLayout passwordLayout = view.findViewById(R.id.passwordLayout);
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
            String email = String.valueOf(emailText.getText()).toLowerCase().trim();
            String password = String.valueOf(passwordText.getText());

            // Clear any error messages
            emailLayout.setError(null);
            passwordLayout.setError(null);

            // Check if the email is empty
            if (email.isEmpty()) {
                emailLayout.setError("Email is required");
                return;
            }

            // Check if the email is valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.setError("Please enter a valid email address");
                return;
            }

            // Check if the password is empty
            if (password.isEmpty()) {
                passwordLayout.setError("Password is required");
                return;
            }




            // TODO: FOR DEBUGGING PURPOSES ONLY:
            // Log the email and password entered by the user
            Log.d("LoginFragment", "Email entered: " + email);
            Log.d("LoginFragment", "Password entered: " + password);


            // Hash the password
            String hashedPassword = null;

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
                hashedPassword = Base64.encodeToString(hash, Base64.DEFAULT).trim();
                Log.d("LoginFragment", "Hashed password: " + hashedPassword);
            } catch (NoSuchAlgorithmException e) {
                Log.i("LoginFragment", "Hashing algorithm not found" + e.getMessage());
            }

            // Check if the email exists in the database
            String[] columns = {"email_address"};
            String selection = "email_address=?";
            String[] selectionArgs = {email};
            Cursor cursor = db.getReadableDatabase().query("User", columns, selection, selectionArgs, null, null, null);
            boolean emailExists = (cursor.getCount() > 0);

            // Log the result of the email check
            Log.d("LoginFragment", "Email exists in database: " + emailExists);

            if (!emailExists) {
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_LONG).show();
                return;
            }

            // Check if the password matches the email in the database
            columns = new String[]{"password"};
            selection = "email_address=? AND password=?";
            selectionArgs = new String[]{email, hashedPassword};
            cursor = db.getReadableDatabase().query("User", columns, selection, selectionArgs, null, null, null);
            boolean passwordMatches = (cursor.getCount() > 0);

            // Log the result of the password check
            Log.d("LoginFragment", "Password matches email in database: " + passwordMatches);

            cursor.close();
            if (!passwordMatches) {
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_LONG).show();
//                loginErrorTextView.setVisibility(View.VISIBLE);
                return;
            }

            // User successfully logged in
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("email", email);
            editor.apply();
            Log.d("LoginFragment", "User logged in as " +email);

            changeFragment(new HomeFragment());
        });


        // Continue as a guest
        TextView continueAsGuestLogin = view.findViewById(R.id.continueAsGuestLogin);

        continueAsGuestLogin.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            Log.d("LoginFragment", "User logged in as guest");

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

        NotificationHelper.createNotificationChannel(getContext());

    }



    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}