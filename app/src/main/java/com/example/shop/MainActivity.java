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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        checkLoginStatus();
    }

    /**
     * Redirect to the login or dashboard activity based on whether the user is logged in or not
     */
    private void checkLoginStatus() {
        Intent intent = new Intent(MainActivity.this, sessionManager.isLoggedIn() ? DashboardActivity.class : LoginActivity.class);
        startActivity(intent);
        finish();
    }
}