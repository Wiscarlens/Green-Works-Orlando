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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.enableNavigationViews(View.GONE);

        TextInputEditText emailTIET = view.findViewById(R.id.emailText);
        TextInputLayout emailTIL = view.findViewById(R.id.emailLayout);
        TextInputEditText passwordTIET = view.findViewById(R.id.passwordText);
        TextInputLayout passwordTIL = view.findViewById(R.id.passwordLayout);
        TextView forgotPasswordTV = view.findViewById(R.id.forgotPassword);
        LinearLayout signUpLL = view.findViewById(R.id.signUpLoginLL);
        Button loginButton = view.findViewById(R.id.loginButton);
        TextView continueAsGuestTV = view.findViewById(R.id.continueAsGuestLogin);

        forgotPasswordTV.setOnClickListener(view1 -> {
            ResetPasswordFragment resetPassword = new ResetPasswordFragment();
            resetPassword.show(getParentFragmentManager(), resetPassword.getTag());
        });

        signUpLL.setOnClickListener(view1 -> {
            RegistrationFragment registrationFragment = new RegistrationFragment();
            registrationFragment.show(getParentFragmentManager(), registrationFragment.getTag());
        });

        loginButton.setOnClickListener(view1 -> {
            loginButton.setClickable(false);

            String email = String.valueOf(emailTIET.getText()).toLowerCase().trim();
            String password = String.valueOf(passwordTIET.getText());

            // Clear any error messages
            emailTIL.setError(null);
            passwordTIL.setError(null);

            // Check if the email is empty
            if (email.isEmpty()) {
                emailTIL.setError("Email is required");
                loginButton.setClickable(true);
                return;
            }

            // Check if the email is valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTIL.setError("Please enter a valid email address");
                loginButton.setClickable(true);
                return;
            }

            // Check if the password is empty
            if (password.isEmpty()) {
                passwordTIL.setError("Password is required");
                loginButton.setClickable(true);
                return;
            }

            // TODO: Check if password is valid

            // Check Network Connection
            if (!NetworkManager.isNetworkAvailable(requireContext())) {
                Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
                loginButton.setClickable(true);
                return;
            }

            MainActivity.mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                        requireActivity().recreate(); // Refresh MainActivity

                    }).addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                    );

            loginButton.setClickable(true);

        });


        continueAsGuestTV.setOnClickListener(v -> {
            UIController uiController = new UIController(getActivity());
            uiController.changeFragment(new HomeFragment());
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

        NotificationHelper.createNotificationChannel(getContext());

    }
}