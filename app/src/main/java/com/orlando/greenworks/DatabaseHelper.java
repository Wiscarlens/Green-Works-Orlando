package com.orlando.greenworks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "greenworks_orlando_db";
    // Increment the database version to trigger onUpgrade
    private static final int DATABASE_VERSION = 1;

    // SQL statement to create the User table
    private static final String CREATE_TABLE_USER = "CREATE TABLE User (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email_address TEXT, " +
            "password TEXT, " +
            "first_name TEXT, " +
            "last_name TEXT, " +
            "phone_number INTEGER, " +
            "address TEXT)";

    // SQL statement to create the Badge table
    private static final String CREATE_TABLE_BADGE = "CREATE TABLE Badge (" +
            "badge_id INTEGER PRIMARY KEY, " +
            "badge_name TEXT, " +
            "badge_description TEXT, " +
            "badge_total_points_required INTEGER, " +
            "badge_image BLOB)";

    // SQL statement to create the User_Badge table
    private static final String CREATE_TABLE_USER_BADGE = "CREATE TABLE User_Badge (" +
            "user_badge_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "badge_id INTEGER, " +
            "user_id INTEGER, " +
            "date_earned TEXT, " +
            "progress INTEGER)";

    // SQL statement to create the User table
    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE Items (" +
            "item_id INTEGER PRIMARY KEY, " +
            "item_title TEXT, " +
            "item_description TEXT, " +
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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BADGE);
        db.execSQL(CREATE_TABLE_USER_BADGE);
        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_USER_ITEMS);
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
}

