package com.orlando.greenworks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutionException;
import java.lang.Exception;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import android.util.Base64;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */


public class DatabaseHelper extends SQLiteOpenHelper {

    // Store images in the database as BLOBs
    public byte[] getBitmapAsByteArray(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    private static final String DATABASE_NAME = "greenworks_orlando_db";

    // Increment the database version to trigger onUpgrade when the schema changes
    private static final int DATABASE_VERSION = 22;


    // Create Tables
    // SQL statement to create the User table
    private static final String CREATE_TABLE_USER = "CREATE TABLE User (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email_address TEXT, " +
            "password TEXT, " +
            "totalPoints INTEGER, " +
            "first_name TEXT, " +
            "last_name TEXT, " +
            "phone_number INTEGER, " +
            "apt_suite TEXT, " +
            "address TEXT)";

    // SQL statement to create Items table
    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE Items (" +
            "item_id INTEGER PRIMARY KEY, " +
            "item_title TEXT, " +
            "item_description TEXT, " +
            "recycle_tag TEXT, " +
            "item_points INTEGER, " +
            "item_image BLOB)";

    // SQL statement to create the User_Items table
    private static final String CREATE_TABLE_USER_ITEMS = "CREATE TABLE User_Items (" +
            "user_item_id INTEGER PRIMARY KEY, " +
            "item_id INTEGER, " +
            "item_title TEXT, " +
            "item_description TEXT, " +
            "current_points INTEGER, " +
            "item_image BLOB)";


    // SQL statement to create the Badge table
    private static final String CREATE_TABLE_BADGE = "CREATE TABLE Badge (" +
            "badge_id INTEGER PRIMARY KEY, " +
            "badge_name TEXT, " +
            "badge_description TEXT, " +
            "badge_total_points_required INTEGER, " +
            "badge_image BLOB)";


    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BADGE);
        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_USER_ITEMS);

        // Add new badges here
        ContentValues values = new ContentValues();
        String[] badgeNames = {
                "Recruit Recycler",
                "Initiate Recycler",
                "Adept Recycler",
                "Heroic Recycler",
                "Epic Recycler",
                "Legendary Recycler",
                "Mythic Recycler",
                "Ultimate Recycler"
        };
        String[] badgeDescriptions = {
                "Description for Recruit Recycler",
                "Description for Initiate Recycler",
                "Description for Adept Recycler",
                "Description for Heroic Recycler",
                "Description for Epic Recycler",
                "Description for Legendary Recycler",
                "Description for Mythic Recycler",
                "Description for Ultimate Recycler"
        };
        int[] badgePoints = {100, 250, 400, 550, 700, 850, 900, 1000};

        for (int i = 0; i < badgeNames.length; i++) {
            values.put("badge_id", i + 1);
            values.put("badge_name", badgeNames[i]);
            values.put("badge_description", badgeDescriptions[i]);
            values.put("badge_total_points_required", badgePoints[i]);
            db.insert("Badge", null, values);
        }


        // Insert a test user
        ContentValues userValues = new ContentValues();
        userValues.put("email_address", "test@test.com");
        // Hash the password
        String testPassword = "Test123!";
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(testPassword.getBytes(StandardCharsets.UTF_8));
            hashedPassword = Base64.encodeToString(hash, Base64.DEFAULT).trim();
        } catch (NoSuchAlgorithmException e) {
            Log.e("DatabaseHelper", "Error occurred during password hashing", e);
        }
        userValues.put("password", hashedPassword);
        userValues.put("totalPoints", 250);
        userValues.put("first_name", "Test");
        userValues.put("last_name", "Test");
        userValues.put("phone_number", "(555) 555-5555");
        userValues.put("apt_suite", "00");
        userValues.put("address", "1800 South Kirkman Road, Orlando, FL, USA");
        db.insert("User", null, userValues);


        populateItemsTable();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema migration logic here
        // Example: if(oldVersion < 2) { // apply changes that were added in version 2 }

        //
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Badge");
        db.execSQL("DROP TABLE IF EXISTS User_Badge");
        db.execSQL("DROP TABLE IF EXISTS Items");
        db.execSQL("DROP TABLE IF EXISTS User_Items");
        onCreate(db);

    }

    public void populateItemsTable() {
        Log.d("DatabaseHelper", "populateItemsTable() called");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SQLiteDatabase db = getWritableDatabase();
                for (int i = 1; i <= 100; i++) {
                    try {
                        URL url = new URL("https://curry-home.cheetoh-python.ts.net/items/" + i);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Accept", "application/json");

                        int responseCode = connection.getResponseCode();
                        Log.d("DB_API_Response_Code", String.valueOf(responseCode));

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String inputLine;
                            StringBuilder response = new StringBuilder();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            Log.d("DB_API_Response", response.toString());

                            JSONObject item = new JSONObject(response.toString());
                            String name = item.optString("name");
                            String description = item.optString("description");
                            JSONArray tags = item.optJSONArray("tags");
                            String recycleTag = tags != null && tags.length() > 0 ? tags.getJSONObject(0).optString("name") : "";

                            ContentValues values = new ContentValues();
                            values.put("item_id", i);
                            values.put("item_title", name);
                            values.put("item_description", description);
                            values.put("recycle_tag", recycleTag);


                            // Set item_points based on item_id
                            // Must be done manually for each new recyclable item added to API, as API does not have item points yet
                            if (i == 1) {
                                values.put("item_points", 10);
                            } else if (i == 10 || i == 11) {
                                values.put("item_points", 5);
                            } else if (i == 9) {
                                values.put("item_points", 15);
                            } else {
                                values.put("item_points", 0);
                            }


                            // Set item_image based on item_id
                            // Must be done manually for each new item added to API, as API does not have item images yet
                            if (i == 1) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_1_soda_can));
                            } else if (i == 2) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_2_plastic_bag));
                            } else if (i == 3) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_3_laptop));
                            } else if (i == 4) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_4_desktop_computer));
                            } else if (i == 5) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_5_face_mask));
                            } else if (i == 6) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_6_food));
                            } else if (i == 7) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_7_broken_glass));
                            } else if (i == 8) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_8_pet_food_bags));
                            } else if (i == 9) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_9_clothes));
                            } else if (i == 10) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_10_paper));
                            } else if (i == 11) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_11_cardboard_box));
                            } else if (i == 12) {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.item_id_12_disposable_battery));
                            } else {
                                values.put("item_image", getBitmapAsByteArray(R.drawable.not_found));
                            }

                            long result = db.insert("Items", null, values);
                            if (result == -1) {
                                Log.e("DB_Insert_Error", "Failed to insert item with id: " + i);
                            } else {
                                Log.d("DB_Insert_Success", "Successfully inserted item with id: " + i);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("DB_API_Error", "Error at item id: " + i, e);
                    }
                }

                return null;
            }
        }.execute();
    }
}