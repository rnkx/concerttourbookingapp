package com.example.a6012cem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ConcertBooking.db";
    private static final int DATABASE_VERSION = 1;

    // Admin table
    private static final String TABLE_ADMINS = "admins";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CREATED_AT = "created_at";

    // Create admin table query
    private static final String CREATE_ADMINS_TABLE = "CREATE TABLE " + TABLE_ADMINS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FULL_NAME + " TEXT NOT NULL,"
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMINS_TABLE);

        // Insert a default admin for testing
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, "System Administrator");
        values.put(COLUMN_EMAIL, "admin@concert.com");
        values.put(COLUMN_PASSWORD, "admin123"); // In real app, hash this password
        db.insert(TABLE_ADMINS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINS);
        onCreate(db);
    }

    // Add new admin
    public boolean addAdmin(String fullName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password); // Note: In production, hash the password

        long result = db.insert(TABLE_ADMINS, null, values);
        return result != -1;
    }

    // Check if admin exists
    public boolean checkAdminExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Verify admin login
    public boolean verifyAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password},
                null, null, null);

        boolean verified = cursor.getCount() > 0;
        cursor.close();
        return verified;
    }

    // Get admin by email
    public Admin getAdminByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,
                new String[]{COLUMN_ID, COLUMN_FULL_NAME, COLUMN_EMAIL, COLUMN_CREATED_AT},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Admin admin = new Admin(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))
            );
            cursor.close();
            return admin;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Get all admins (for admin management)
    public List<Admin> getAllAdmins() {
        List<Admin> adminList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMINS + " ORDER BY " + COLUMN_CREATED_AT + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Admin admin = new Admin(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))
                );
                adminList.add(admin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adminList;
    }
}