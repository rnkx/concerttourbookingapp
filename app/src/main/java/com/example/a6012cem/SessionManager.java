package com.example.a6012cem;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "AdminSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ADMIN_ID = "adminId";
    private static final String KEY_ADMIN_NAME = "adminName";
    private static final String KEY_ADMIN_EMAIL = "adminEmail";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int adminId, String adminName, String adminEmail) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_ADMIN_ID, adminId);
        editor.putString(KEY_ADMIN_NAME, adminName);
        editor.putString(KEY_ADMIN_EMAIL, adminEmail);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getAdminId() {
        return pref.getInt(KEY_ADMIN_ID, -1);
    }

    public String getAdminName() {
        return pref.getString(KEY_ADMIN_NAME, "");
    }

    public String getAdminEmail() {
        return pref.getString(KEY_ADMIN_EMAIL, "");
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}