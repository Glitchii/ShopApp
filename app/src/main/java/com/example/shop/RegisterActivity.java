package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword, etHobbies, etPostcode, etAddress;
    Button btnRegister;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etHobbies = findViewById(R.id.et_hobbies);
        etPostcode = findViewById(R.id.et_postcode);
        etAddress = findViewById(R.id.et_address);
        btnRegister = findViewById(R.id.btn_register);

        sqLiteHelper = new SQLiteHelper(this);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    /**
     * Registers a new user with the input data.
     */
    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String hobbies = etHobbies.getText().toString().trim();
        String postcode = etPostcode.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (areFieldsFilledOut()) {
            long newRowId = sqLiteHelper.addUser(fullName, email, password, hobbies, postcode, address);

            if (newRowId > 0) {
                Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                SessionManager sessionManager = new SessionManager(RegisterActivity.this);
                sessionManager.loginUser(email);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error registering user. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Helper method to check if all the input fields are filled out.
     *
     * @return true if all fields are filled out, false otherwise
     */
    private boolean areFieldsFilledOut() {
        return !TextUtils.isEmpty(etFullName.getText().toString().trim())
               && !TextUtils.isEmpty(etEmail.getText().toString().trim())
               && !TextUtils.isEmpty(etPassword.getText().toString().trim())
               && !TextUtils.isEmpty(etHobbies.getText().toString().trim())
               && !TextUtils.isEmpty(etPostcode.getText().toString().trim())
               && !TextUtils.isEmpty(etAddress.getText().toString().trim());
    }
}
