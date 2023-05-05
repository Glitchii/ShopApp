package com.example.shop;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BasketActivity extends AppCompatActivity {

    private RecyclerView rvBasketItems;
    private TextView tvTotalCost;
    private SQLiteHelper sqLiteHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        rvBasketItems = findViewById(R.id.rv_basket_items);
        tvTotalCost = findViewById(R.id.tv_total_cost);

        sqLiteHelper = new SQLiteHelper(this);
        sessionManager = new SessionManager(this);

        // Get the product IDs from the user's basket
        String basketString = sessionManager.getUserBasket();
        List<String> basketProductIds = Arrays.asList(basketString.split(","));

        // Fetch the products from the database
        List<Product> basketProducts = sqLiteHelper.getProductsByIds(basketProductIds);

        // Calculate the total cost
        double totalCost = 0;
        for (Product product : basketProducts)
            totalCost += product.getPrice();

        // Update the total cost TextView
        tvTotalCost.setText(String.format(Locale.getDefault(), "Total cost: £%.2f", totalCost));

        // Create and set the adapter for the RecyclerView
        DashboardItemAdapter dashboardItemAdapter = new DashboardItemAdapter(this, basketProducts, true);
        rvBasketItems.setAdapter(dashboardItemAdapter);

        // Set the layout manager for the RecyclerView
        rvBasketItems.setLayoutManager(new LinearLayoutManager(this));
    }
}

