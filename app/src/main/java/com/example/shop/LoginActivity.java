package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btnRegister;
    SQLiteHelper sqLiteHelper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        sqLiteHelper = new SQLiteHelper(this);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Logs in the user and saves their login state if their credentials are valid.
     */
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean isUserValid = sqLiteHelper.checkUserCredentials(email, password);

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
            Toast.makeText(this, "Please enter your email and password.", Toast.LENGTH_SHORT).show();
        else if (!isUserValid)
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        else {
            SessionManager sessionManager = new SessionManager(LoginActivity.this);
            sessionManager.loginUser(email);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
