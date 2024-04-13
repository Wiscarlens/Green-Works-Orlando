package com.orlando.greenworks;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// TODO: More thorough email address validation

public class RegistrationFragment extends BottomSheetDialogFragment {
  private AutoCompleteTextView addressAC;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        TextInputLayout firstNameTIL = view.findViewById(R.id.firstNameLayout);
        TextInputEditText firstNameTIET = view.findViewById(R.id.firstName);
        TextInputLayout lastNameTIL = view.findViewById(R.id.lastNameLayout);
        TextInputEditText lastNameTIET = view.findViewById(R.id.lastName);
        TextInputLayout addressLayout = view.findViewById(R.id.addressLayout);
        addressAC = view.findViewById(R.id.addressAutoComplete);
        TextInputEditText suiteTIET = view.findViewById(R.id.aptSuiteText);
        TextInputEditText emailTIET = view.findViewById(R.id.emailText);
        TextInputLayout emailLayout = view.findViewById(R.id.emailLayout);
        TextInputLayout phoneNumberLayout = view.findViewById(R.id.phoneNumberLayout);
        TextInputEditText phoneNumberTIET = view.findViewById(R.id.phoneNumberText);
        TextInputLayout passwordLayout = view.findViewById(R.id.passwordLayout);
        TextInputEditText passwordTIET = view.findViewById(R.id.passwordText);
        TextInputLayout confirmPasswordLayout = view.findViewById(R.id.passwordConfirmLayout);
        TextInputEditText confirmPasswordTIET = view.findViewById(R.id.passwordConfirmText);
        CheckBox termCheckBox = view.findViewById(R.id.termCheckBox);
        TextView termsAndConditionsTV = view.findViewById(R.id.termsConditions);
        Button createAccountButton = view.findViewById(R.id.createAccountButton);
        TextView loginTV = view.findViewById(R.id.loginLink);

        loginTV.setOnClickListener(v -> dismiss());

        termsAndConditionsTV.setOnClickListener(v -> {
            TermsFragment termsFragment = new TermsFragment();
            termsFragment.show(getChildFragmentManager(), termsFragment.getTag());
        });

        closeButton.setOnClickListener(v -> dismiss());

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        addressAC.setAdapter(adapter);
        addressAC.setThreshold(1);
        addressAC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchSuggestions(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    addressAC.setEnabled(true);
                }
            }
        });

        // Disable editing after an item is selected from the dropdown
        addressAC.setOnItemClickListener((parent, view1, position, id) -> {
            addressAC.setEnabled(false);
        });
        // Re-enable editing when the AutoCompleteTextView gains focus
        // Clear the field if the text is not part of the suggestions when it loses focus
        addressAC.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = addressAC.getText().toString();
                if (adapter.getPosition(input) == -1) {
                    addressAC.setText("");
                }
            } else {
                addressAC.setEnabled(true);
            }
        });

        phoneNumberTIET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                formatPhoneNumber(s);
            }
        });



        // Initialize the AutoCompleteTextView for address field
//        addressAC = view.findViewById(R.id.addressAutoComplete);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        addressAC.setAdapter(adapter);
        addressAC.setThreshold(1);

        addressAC.addTextChangedListener(new TextWatcher() {
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
//        Button createAccountButton = view.findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountButton.setClickable(false);

                String firstName = String.valueOf(firstNameTIET.getText()).trim();
                String lastName = String.valueOf(lastNameTIET.getText()).trim();
                String emailAddress = String.valueOf(emailTIET.getText()).trim();
                String password = String.valueOf(passwordTIET.getText());
                String confirm_password = String.valueOf(confirmPasswordTIET.getText());
                String address = String.valueOf(addressAC.getText()).trim();
                String suite = String.valueOf(suiteTIET.getText()).trim();
                String phoneNumber = String.valueOf(phoneNumberTIET.getText()).trim();
                boolean termAgree = termCheckBox.isChecked();

                clearAllErrors(firstNameTIL, lastNameTIL, addressLayout, emailLayout, phoneNumberLayout, passwordLayout, confirmPasswordLayout);

                // Check if the required fields are empty
                isFieldEmpty(firstName, firstNameTIL, "First name is required");
                isFieldEmpty(lastName, lastNameTIL, "Last name is required");
                isFieldEmpty(emailAddress, emailLayout, "Email is required");
                isFieldEmpty(password, passwordLayout, "Password is required");
                isFieldEmpty(confirm_password, confirmPasswordLayout, "Confirm password is required");
//                isFieldEmpty(address, addressLayout, "Address is required");
                isFieldEmpty(phoneNumber, phoneNumberLayout, "Phone number is required");


//                boolean errorOccurred = false;

//                // Sanitize and validate the user input fields
//                if (emailExistsInDatabase(emailAddress)) {
//                    // TODO: not necessary to check if phone number exists in database
//                    Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
//                    createAccountButton.setClickable(true);
//                    return;
//                }
//
//                if (phoneNumberExistsInDatabase(phoneNumber)) {
//                    // TODO: not necessary to check if phone number exists in database
//                    Toast.makeText(getContext(), "Phone number already exists", Toast.LENGTH_SHORT).show();
//                    createAccountButton.setClickable(true);
//                    return;
//                }


                if (!isNameValid(firstName, firstNameTIL)) {
                    createAccountButton.setClickable(true);
                    return;
                }

                if (!isNameValid(lastName, lastNameTIL)) {
                    createAccountButton.setClickable(true);
                    return;
                }


//
//                if (adapter.getPosition(address) == -1) {
//                    addressLayout.setError("Address is invalid");
//                    createAccountButton.setClickable(true);
//                    return;
//                }

                if (!isEmailValid(emailAddress, emailLayout)) {
                    createAccountButton.setClickable(true);
                    return;
                }



                if (phoneNumber.length() != 14){
                    phoneNumberLayout.setError("Phone number is too short");
                    createAccountButton.setClickable(true);
                    return;
                }
//
//                if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,12}$")) {
//                    passwordLayout.setError("Password is invalid");
//                   createAccountButton.setClickable(true);
//                    return;
//                }
//
                if (!password.equals(confirm_password)) {
                    confirmPasswordLayout.setError("Passwords do not match");
                    createAccountButton.setClickable(true);
                    return;
                }

                if (!termAgree) {
                    Toast.makeText(getContext(), "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                   createAccountButton.setClickable(true);
                    return;
                } else {
                    // If all checks pass then create the account and store it in the database, then proceed to login with success message
                    // Hash the password
//                    String hashedPassword = null;
//                    try {
//                        Log.d("RegistrationFragment", "Starting password hashing process");
//                        MessageDigest md = MessageDigest.getInstance("SHA-256");
//                        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
//                        hashedPassword = Base64.encodeToString(hash, Base64.DEFAULT).trim();
//                        Log.d("RegistrationFragment", "Password hashed successfully");
//                    } catch (NoSuchAlgorithmException e) {
//                        Log.e("RegistrationFragment", "Error occurred during password hashing", e);
//                        // ... handle the exception ...
//                    }

//                    // Store the data in the database
//                    DatabaseHelper db = new DatabaseHelper(getContext());
//                    ContentValues values = new ContentValues();
//                    values.put("first_name", firstName);
//                    values.put("last_name", lastName);
//                    values.put("email_address", emailAddress);
//                    values.put("password", hashedPassword);
//                    values.put("address", address);
//                    values.put("apt_suite", aptSuite);
//                    values.put("phone_number", phoneNumber);
//
//                    long result = db.getWritableDatabase().insert("User", null, values);
//
//                    if (result == -1) {
//                        Log.e("RegistrationFragment", "Error inserting data into the database");
//                    } else {
//                        Log.d("RegistrationFragment", "Data inserted into the database successfully");
//                    }

                    FirebaseHandler mAuth = new FirebaseHandler();

                    mAuth.createUser(
                            new User(firstName, lastName, address, suite, emailAddress, phoneNumber, password),
                            getContext()
                    );

                    createAccountButton.setClickable(true);

                    // Open Home Fragment
                    UIController uiController = new UIController(requireActivity());
                    uiController.changeFragment(new HomeFragment());
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
                requireActivity().runOnUiThread(() -> {
                    adapter.clear();
                    adapter.addAll(suggestions);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Log.e("fetchSuggestions", "Error fetching suggestions: " + e.getMessage(), e);
            }
        }).start();
    }

    public void isFieldEmpty(String fieldValue, TextInputLayout fieldLayout, String errorMessage) {
        if (fieldValue.isEmpty()) {
            fieldLayout.setError(errorMessage);
        } else {
            fieldLayout.setError(null);
        }
    }

    private void clearAllErrors(TextInputLayout... layouts) {
        for (TextInputLayout layout : layouts) {
            layout.setError(null);
        }
    }



    public void formatPhoneNumber(Editable s) {
        String str = s.toString();
        char dash = '-';
        char openParen = '(';
        char closeParen = ')';
        int len1 = 1;
        int len5 = 5;
        int len6 = 6;
        int len10 = 10;
        int len14 = 14;

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

    private boolean isNameValid(String name, TextInputLayout nameTIL) {
        if (!name.matches("^[a-zA-Z]*$") || name.length() > 30) {
            nameTIL.setError("Name is invalid");
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email, TextInputLayout emailLayout) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Email is invalid");
            return false;
        }
        return true;
    }


    // Check if the account / email address / phone number already exists in the database. If yes, flag as duplicate account.
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
    private boolean phoneNumberExistsInDatabase(String phoneNumber) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        String[] columns = {"phone_number"};
        String selection = "phone_number=?";
        String[] selectionArgs = {phoneNumber};
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
            Log.e("RegistrationFragment", "Error occurred during password hashing", e);
            return null;
        }
    }
}