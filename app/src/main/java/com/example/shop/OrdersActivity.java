package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private SQLiteHelper sqLiteHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        rvOrders = findViewById(R.id.rv_orders);
        sqLiteHelper = new SQLiteHelper(this);
        sessionManager = new SessionManager(this);

        // Get the current user
        User user = sessionManager.getUserDetails(this);

        // Fetch orders based on user role
        List<Order> orders = sqLiteHelper.getOrdersByUser(user.getId(), user.isAdmin());

        // Create and set the adapter for the RecyclerView
        OrdersAdapter ordersAdapter = new OrdersAdapter(this, orders);
        rvOrders.setAdapter(ordersAdapter);

        // Set the layout manager for the RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
    }
}
