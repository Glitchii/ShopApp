package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

        sessionManager = new SessionManager(this);
        sqLiteHelper = new SQLiteHelper(this);

        // Add test data to the database
        // sqLiteHelper.addCategory("racing", "Racing games");
        // sqLiteHelper.addProduct(1, "F1 2019", "F1 2019 is the official video game of the 2019 Formula One and Formula 2 Championships developed and published by Codemasters.", 59.99, 59.99, 59.99);

        // Add the ProductsFragment to the products_fragment_container
        ProductsFragment productsFragment = new ProductsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.products_fragment_container, productsFragment)
                .commit();

        btnLogout.setOnClickListener(v -> {
                sessionManager.logoutUser();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        });
    }
}