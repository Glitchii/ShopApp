package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private SQLiteHelper sqLiteHelper;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHelper = new SQLiteHelper(this);
        sessionManager = new SessionManager(this);

        // // Insert a test user
        // sqLiteHelper.addUser("John Doe", "john.doe@example.com", "password123", "Reading, Hiking", "12345", "68 Bedford Rd.");

        // // Get all users
        // List<User> users = sqLiteHelper.getAllUsers();
        // for (User user : users) {
        //     Log.d("app--User", "ID: " + user.getId() + ", Name: " + user.getFullName() + ", Email: " + user.getEmail());
        // }

        checkLoginStatus();
    }

    /**
     * Checks if the user is already logged in and performs necessary actions based on the login status.
     */
    private void checkLoginStatus() {
        if (!sessionManager.isLoggedIn()) {
            // Redirect to the LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Redirect to the DashboardActivity
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }
}