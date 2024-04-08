package com.orlando.greenworks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import com.orlando.greenworks.BuildConfig;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.content.ContentValues;
import android.widget.EditText;
import android.util.Log;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import android.widget.Toast;
import android.database.Cursor;
import com.orlando.greenworks.DatabaseHelper;
import android.widget.ScrollView;
import android.widget.CheckBox;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;


// TODO: More thorough email address validation
// TODO: address field do not allow manual address entry, only select from dropdown, but how to handle apartments and suites?
// TODO: Security Audit

public class RegistrationFragment extends BottomSheetDialogFragment {
    // Initialize the AutoCompleteTextView for address
    private AutoCompleteTextView addressAutoComplete;
    private ArrayAdapter<String> adapter;
    private TextView errorTextView;
    private TextView emptyfieldErrorTextView;
    private TextView firstNameErrorTextView;
    private TextView lastNameErrorTextView;
    private TextView addressErrorTextView;
    private TextView weakPasswordErrorTextView;
    private TextView matchPasswordErrorTextView;
    private TextView emailErrorTextView;
    private TextView phoneErrorTextView;
    private TextView accountExistsErrorTextView;
    private TextView termsAgreeErrorTextView;
    private static final String API_KEY = BuildConfig.GMP_KEY;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

    // Fetch address suggestions from Google Places API
    // If address suggestions do not show:
    // If your API key is restricted, please make sure it is correctly configured and can call the Places API.
    private void fetchSuggestions(String input) {
        new Thread(() -> {
            try {
                String encodedInput = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
                String urlString = BASE_URL + "?input=" + encodedInput + "&types=address&components=country:US&key=" + API_KEY;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();

                // Log the response
                Log.d("fetchSuggestions", "API Response: " + response.toString());

                JSONObject json = new JSONObject(response.toString());
                JSONArray predictions = json.getJSONArray("predictions");
                List<String> suggestions = new ArrayList<>();
                for (int i = 0; i < predictions.length(); i++) {
                    String suggestion = predictions.getJSONObject(i).getString("description");
                    suggestions.add(suggestion);
                }
                // Update the adapter on the main thread
                getActivity().runOnUiThread(() -> {
                    adapter.clear();
                    adapter.addAll(suggestions);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Log.e("fetchSuggestions", "Error fetching suggestions: " + e.getMessage(), e);
                e.printStackTrace();
            }
        }).start();
    }

    // Check if the account / email address already exists in the database
    private boolean emailExistsInDatabase(String emailAddress) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        String[] columns = {"email_address"};
        String selection = "email_address=?";
        String[] selectionArgs = {emailAddress};
        Cursor cursor = db.getReadableDatabase().query("User", columns, selection, selectionArgs, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Hash the user password
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);



        // Initialize the close button, terms and conditions link, and login link
        ImageButton closeButton = view.findViewById(R.id.closeButton);
        TextView termsAndConditions = view.findViewById(R.id.termsConditions);
        TextView login = view.findViewById(R.id.loginLink);

        login.setOnClickListener(v -> dismiss());

        termsAndConditions.setOnClickListener(v -> {
            TermsFragment termsFragment = new TermsFragment();
            termsFragment.show(getChildFragmentManager(), termsFragment.getTag());
        });

        closeButton.setOnClickListener(v -> dismiss());


        // Address must be selected from Google Places suggestions
        // Initialize the AutoCompleteTextView for address field
        addressAutoComplete = view.findViewById(R.id.addressAutoComplete);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        addressAutoComplete.setAdapter(adapter);
        addressAutoComplete.setThreshold(1);
        addressAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchSuggestions(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    addressAutoComplete.setEnabled(true);
                }
            }
        });
        // Disable editing after an item is selected from the dropdown
        addressAutoComplete.setOnItemClickListener((parent, view1, position, id) -> {
            addressAutoComplete.setEnabled(false);
        });
        // Re-enable editing when the AutoCompleteTextView gains focus
        // Clear the field if the text is not part of the suggestions when it loses focus
        addressAutoComplete.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = addressAutoComplete.getText().toString();
                if (adapter.getPosition(input) == -1) {
                    addressAutoComplete.setText("");
                }
            } else {
                addressAutoComplete.setEnabled(true);
            }
        });



        // Initialize error TextViews
        TextView errorTextView = view.findViewById(R.id.errorTextView);
        emptyfieldErrorTextView = view.findViewById(R.id.empty_field_errorTextView);
        firstNameErrorTextView = view.findViewById(R.id.first_name_errorTextView);
        lastNameErrorTextView = view.findViewById(R.id.last_name_errorTextView);
        addressErrorTextView = view.findViewById(R.id.address_errorTextView);
        weakPasswordErrorTextView = view.findViewById(R.id.weak_password_errorTextView);
        matchPasswordErrorTextView = view.findViewById(R.id.match_password_errorTextView);
        emailErrorTextView = view.findViewById(R.id.email_errorTextView);
        phoneErrorTextView = view.findViewById(R.id.phone_errorTextView);
        accountExistsErrorTextView = view.findViewById(R.id.account_exists_errorTextView);
        termsAgreeErrorTextView = view.findViewById(R.id.terms_agree_errorTextView);

        // Initialize the phone number field
        TextInputLayout phoneNumberLayout = view.findViewById(R.id.phoneNumberLayout);
        TextInputEditText phoneNumberEditText = (TextInputEditText) phoneNumberLayout.getEditText();


        // Add a TextWatcher to format the phone number as the user types
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            private static final char dash = '-';
            private static final char openParen = '(';
            private static final char closeParen = ')';
            private static final int len1 = 1;
            private static final int len5 = 5;
            private static final int len6 = 6;
            private static final int len10 = 10;
            private static final int len14 = 14;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.isEmpty()) return;
                char c = str.charAt(str.length() - 1);
                if ((c < '0' || c > '9') && c != dash && c != openParen && c != closeParen) {
                    s.delete(str.length() - 1, str.length());
                    return;
                }
                if (str.length() == len1 && c != openParen) {
                    s.insert(0, String.valueOf(openParen));
                } else if (str.length() == len5 && c != closeParen) {
                    s.insert(len5 - 1, String.valueOf(closeParen));
                } else if (str.length() == len6 && c != ' ') {
                    s.insert(len6 - 1, " ");
                } else if (str.length() == len10 && c != dash) {
                    s.insert(len10 - 1, String.valueOf(dash));
                } else if (str.length() > len14) {
                    s.delete(len14, str.length());
                }
            }
        });



        // Initialize the AutoCompleteTextView for address field
        addressAutoComplete = view.findViewById(R.id.addressAutoComplete);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        addressAutoComplete.setAdapter(adapter);
        addressAutoComplete.setThreshold(1);
        addressAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchSuggestions(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });



        // Store registration data in the database and open LoginFragment upon successful account creation
        Button createAccountButton = view.findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Log a message at the start of the onClick method
                Log.d("RegistrationFragment", "Create account button clicked");

                // Get the data from the input fields
                String firstName = ((EditText) view.findViewById(R.id.firstName)).getText().toString();
                String lastName = ((EditText) view.findViewById(R.id.lastName)).getText().toString();
                String emailAddress = ((TextInputLayout) view.findViewById(R.id.emailLayout)).getEditText().getText().toString();
                String password = ((TextInputLayout) view.findViewById(R.id.passwordLayout)).getEditText().getText().toString();
                String confirm_password = ((TextInputLayout) view.findViewById(R.id.passwordConfirmLayout)).getEditText().getText().toString();
                String address = ((AutoCompleteTextView) view.findViewById(R.id.addressAutoComplete)).getText().toString();
                String phoneNumber = ((TextInputLayout) view.findViewById(R.id.phoneNumberLayout)).getEditText().getText().toString();
                CheckBox termCheckBox = view.findViewById(R.id.termCheckBox);


                // Reset the visibility of all error TextViews to GONE
                errorTextView.setVisibility(View.GONE);
                emptyfieldErrorTextView.setVisibility(View.GONE);
                firstNameErrorTextView.setVisibility(View.GONE);
                lastNameErrorTextView.setVisibility(View.GONE);
                addressErrorTextView.setVisibility(View.GONE);
                weakPasswordErrorTextView.setVisibility(View.GONE);
                matchPasswordErrorTextView.setVisibility(View.GONE);
                emailErrorTextView.setVisibility(View.GONE);
                phoneErrorTextView.setVisibility(View.GONE);
                accountExistsErrorTextView.setVisibility(View.GONE);
                termsAgreeErrorTextView.setVisibility(View.GONE);

                boolean errorOccurred = false;

                // Sanitize and validate the user input fields
                if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
                    //Toast.makeText(getContext(), "One or more fields are empty. Please try again.", Toast.LENGTH_LONG).show();
                    errorTextView.setVisibility(View.VISIBLE);
                    emptyfieldErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (emailExistsInDatabase(emailAddress)) {
                    errorTextView.setVisibility(View.VISIBLE);
                    accountExistsErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (!firstName.matches("^[a-zA-Z]*$") || firstName.length() > 30) {
                    errorTextView.setVisibility(View.VISIBLE);
                    firstNameErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (!lastName.matches("^[a-zA-Z]*$") || lastName.length() > 30) {
                    errorTextView.setVisibility(View.VISIBLE);
                    lastNameErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (adapter.getPosition(address) == -1) {
                    errorTextView.setVisibility(View.VISIBLE);
                    addressErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    errorTextView.setVisibility(View.VISIBLE);
                    emailErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (phoneNumber.isEmpty() || !phoneNumber.matches("\\(\\d{3}\\) \\d{3}-\\d{4}")) {
                    errorTextView.setVisibility(View.VISIBLE);
                    phoneErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,12}$")) {
                    errorTextView.setVisibility(View.VISIBLE);
                    weakPasswordErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (!password.equals(confirm_password)) {
                    errorTextView.setVisibility(View.VISIBLE);
                    matchPasswordErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                } else if (!termCheckBox.isChecked()) {
                    errorTextView.setVisibility(View.VISIBLE);
                    termsAgreeErrorTextView.setVisibility(View.VISIBLE);
                    errorOccurred = true;
                    return;
                    // More validation checks as needed
                    // } else if ( Condition here ) {
                    // Validation logic here
                    // errorOccurred = true; // display only one error textview at a time (most recent)
                    // return;
                } else {
                    // If all checks pass then create the account and store it in the database, then proceed to login with success message
                    // Hash the password
                    String hashedPassword = null;
                    try {
                        Log.d("RegistrationFragment", "Starting password hashing process");
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
                        hashedPassword = Base64.encodeToString(hash, Base64.DEFAULT);
                        Log.d("RegistrationFragment", "Password hashed successfully");
                    } catch (NoSuchAlgorithmException e) {
                        Log.e("RegistrationFragment", "Error occurred during password hashing", e);
                        // ... handle the exception ...
                    }

                    // Store the data in the database
                    DatabaseHelper db = new DatabaseHelper(getContext());
                    ContentValues values = new ContentValues();
                    values.put("first_name", firstName);
                    values.put("last_name", lastName);
                    values.put("email_address", emailAddress);
                    values.put("password", hashedPassword);
                    values.put("address", address);
                    values.put("phone_number", phoneNumber);

                    long result = db.getWritableDatabase().insert("User", null, values);
                    if (result == -1) {
                        Log.e("RegistrationFragment", "Error inserting data into the database");
                    } else {
                        Log.d("RegistrationFragment", "Data inserted into the database successfully");
                    }

                    dismiss();

                    LoginFragment loginFragment = new LoginFragment();

                    // Pass a message to the LoginFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("message", getString(R.string.account_success));
                    loginFragment.setArguments(bundle);

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, loginFragment);
                    transaction.commit();

                    if (transaction.isAddToBackStackAllowed()) {
                        Log.d("RegistrationFragment", "Navigated to LoginFragment");
                    } else {
                        Log.e("RegistrationFragment", "Navigation to LoginFragment failed");
                    }

                }


                // Log a message at the end of the onClick method
                Log.d("RegistrationFragment", "Create account button click ended");

            }
        });


        // Inflate the layout for this fragment
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
}