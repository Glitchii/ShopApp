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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    private Button btnLogout;
    private Button btnBasket;
    private Button btnOrders;
    private Button btnCategories;
    private TextView welcomeView;
    private FloatingActionButton fabAddProduct;

    private SessionManager sessionManager;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnLogout = findViewById(R.id.btn_logout);
        btnBasket = findViewById(R.id.btn_basket);
        btnOrders = findViewById(R.id.btn_orders);
        welcomeView = findViewById(R.id.welcome_msg);
        fabAddProduct = findViewById(R.id.fab_add_product);
        btnCategories = findViewById(R.id.btn_categories);

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
        replaceProductsFragment();

        // Fetch user data using the email
        String userEmail = sessionManager.getUserEmail();
        User user = sqLiteHelper.getUserByEmail(userEmail);

        // Set welcome message with the user's full name
        welcomeView.setText(String.format("Welcome, %s!", user.getFullName()));

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnBasket.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BasketActivity.class);
            startActivity(intent);
        });

        btnOrders.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, OrdersActivity.class);
            startActivity(intent);
        });

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CategoriesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        replaceProductsFragment();
    }

    private void replaceProductsFragment() {
        ProductsFragment productsFragment = new ProductsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.products_fragment_container, productsFragment)
                .commit();
    }
}