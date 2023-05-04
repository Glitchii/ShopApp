package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

/**
 * Manages user session data using SharedPreferences.
 */
public class SessionManager {
    private static final String USER_PREFS = "user_prefs";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String EMAIL = "email";
    private SharedPreferences sharedPreferences;
    private Context context;

    /**
     * Initializes the SessionManager with the given context.
     *
     * @param context the context for accessing SharedPreferences
     */
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
    }

    /**
     * Logs in the user and saves their email in SharedPreferences.
     *
     * @param email the email address of the logged-in user
     */
    public void loginUser(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(EMAIL, email);
        editor.apply();
    }

    /**
     * Checks if the user is already logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * Retrieves the email of the logged-in user.
     *
     * @return the email address of the logged-in user, or an empty string if no user is logged in
     */
    public String getUserEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Saves the user's basket in SharedPreferences.
     * 
     * @param basket the user's basket
     */
    public void saveUserBasket(String basket) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("basket", basket);
        editor.apply();
    }

    /**
     * Retrieves the user's basket from SharedPreferences.
     * 
     * @return the user's basket, or an empty string if no basket is saved
     */
    public String getUserBasket() {
        return sharedPreferences.getString("basket", "");
    }

}