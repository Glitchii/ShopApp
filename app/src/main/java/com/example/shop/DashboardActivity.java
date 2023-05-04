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
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity {

    private Button btnLogout;

    private SessionManager sessionManager;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnLogout = findViewById(R.id.btn_logout);

        sessionManager = new SessionManager(this);
        sqLiteHelper = new SQLiteHelper(this);
        
        // // Add random products and name using Random
        // Random random = new Random();
        // for (int i = 0; i < 2; i++) {
        //     sqLiteHelper.addCategory("category" + i, "Category " + i);
        //     for (int j = 0; j < 2; j++) {
        //         sqLiteHelper.addProduct(i + 1, "product" + j, "Product " + j,
        //                 random.nextDouble() * 100, random.nextDouble() * 100, random.nextDouble() *
        //                         100);
        //     }
        // }

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

        Button btnBasket = findViewById(R.id.btn_basket);
        btnBasket.setOnClickListener(v -> {
            // Start the BasketActivity
            Intent intent = new Intent(DashboardActivity.this, BasketActivity.class);
            startActivity(intent);
        });

    }
}