package com.orlando.greenworks;
import android.content.Context;
import android.content.SharedPreferences;

public class LocalDataStorage {

    private static final String PREF_NAME = "UserData";
    private static final String KEY_FIRSTNAME = "FirstName";
    private static final String KEY_LASTNAME= "LastName";
    private static final String KEY_ADDRESS = "FirstName";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONFIRM_PASSWORD = "confirmPassword";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RegistrationFragment context;

    public LocalDataStorage(RegistrationFragment context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public LocalDataStorage(LoginFragment loginFragment) {
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public void saveUserData(String firstName,String lastName,String address, String email, String password, String confirmPassword) {
        editor.putString(KEY_FIRSTNAME, firstName);
        editor.putString(KEY_LASTNAME, lastName);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_CONFIRM_PASSWORD, confirmPassword);
        editor.apply();
    }

    public String[] getUserData() {
        String firstName = sharedPreferences.getString(KEY_FIRSTNAME, "");
        String lastName = sharedPreferences.getString(KEY_LASTNAME, "");
        String address = sharedPreferences.getString(KEY_ADDRESS, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        String confirmPassword = sharedPreferences.getString(KEY_CONFIRM_PASSWORD, "");
        return new String[]{firstName,lastName,address, email, password, confirmPassword};
    }
}

