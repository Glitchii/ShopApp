package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcomeMessage;
    private Button btnLogout;
    
    private SessionManager sessionManager;
    private DashboardItemAdapter itemAdapter;
    private RecyclerView rvItems;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcomeMessage = findViewById(R.id.welcome_msg);
        btnLogout = findViewById(R.id.btn_logout);
        rvItems = findViewById(R.id.rv_items);

        sessionManager = new SessionManager(this);
        sqLiteHelper = new SQLiteHelper(this);

        // Fetch user data using the email
        String userEmail = sessionManager.getUserEmail();
        User user = sqLiteHelper.getUserByEmail(userEmail);

        // Set welcome message with the user's full name
        tvWelcomeMessage.setText(String.format("Welcome, %s!", user.getFullName()));

        // Add test data to the database
        // sqLiteHelper.addCategory("racing", "Racing games");
        // sqLiteHelper.addProduct(1, "F1 2019", "F1 2019 is the official video game of the 2019 Formula One and Formula 2 Championships developed and published by Codemasters.", 59.99, 59.99, 59.99);

        // Fetch the list of products
        List<Product> products = sqLiteHelper.getAllProducts();

        // Create and set the adapter for the RecyclerView
        DashboardItemAdapter dashboardItemAdapter = new DashboardItemAdapter(this, products);
        rvItems.setAdapter(dashboardItemAdapter);

        // Set the layout manager for the RecyclerView
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}