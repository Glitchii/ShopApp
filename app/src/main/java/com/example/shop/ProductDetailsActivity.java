package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView tvProductName;
    private TextView tvProductDescription;
    private TextView tvProductPrice;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnAddToBasket;
    private Button btnRemove;
    private Button btnOrder;
    private TextView tvProductListPrice;
    private TextView tvProductRetailPrice;

    private SQLiteHelper sqLiteHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize UI elements
        tvProductName = findViewById(R.id.product_name);
        tvProductDescription = findViewById(R.id.product_description);
        tvProductPrice = findViewById(R.id.product_price);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnAddToBasket = findViewById(R.id.btn_add_to_basket);
        tvProductListPrice = findViewById(R.id.product_list_price);
        tvProductRetailPrice = findViewById(R.id.product_retail_price);
        btnRemove = findViewById(R.id.btn_remove); // Remove from basket button
        btnOrder = findViewById(R.id.btn_order);

        Intent intent = getIntent();
        int productId = intent.getIntExtra("PRODUCT_ID", 0);
        boolean fromBasketActivity = intent.getBooleanExtra("FROM_BASKET_ACTIVITY", false);

        if (fromBasketActivity) {
            // Hide the product page buttons if the user is coming from the basket activity
            btnAddToBasket.setVisibility(View.GONE);
            btnDelete.setVisibility(Button.GONE);
            btnEdit.setVisibility(Button.GONE);

            // Show basket page buttons
            btnRemove.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
        }

        // Initialize SQLiteHelper and SessionManager
        sqLiteHelper = new SQLiteHelper(this);
        sessionManager = new SessionManager(this);

        // Fetch the product from the database
        Product product = sqLiteHelper.getProductById(productId);

        // Set the product information in the UI elements
        tvProductName.setText("Name: " + product.getName());
        tvProductDescription.setText("Description: " + product.getDescription());
        tvProductPrice.setText(String.format("Price £%.2f", product.getPrice()));
        tvProductListPrice.setText(String.format("List Price: £%.2f", product.getListPrice()));
        tvProductRetailPrice.setText(String.format("Retail Price: £%.2f", product.getRetailPrice()));

        // Check if the user is an admin
        User user = sessionManager.getUserDetails(this);

        // Implement the edit and delete functionality
        btnEdit.setOnClickListener(v -> {
            if (!user.isAdmin()) {
                Toast.makeText(this, "Admin action only. Register or login as admin", Toast.LENGTH_LONG).show();
                return;
            }

            Intent editIntent = new Intent(ProductDetailsActivity.this, EditProductActivity.class);
            editIntent.putExtra("PRODUCT_ID", product.getId());
            startActivity(editIntent);
        });

        btnDelete.setOnClickListener(v -> {
            if (!user.isAdmin()) {
                Toast.makeText(this, "Admin action only. Register or login as admin", Toast.LENGTH_LONG).show();
                return;
            }

            deleteProduct(product);
        });

        // Set a click listener for the add to basket button
        btnAddToBasket.setOnClickListener(v -> addToBasket(product.getId()));

        // Click listener to remove item from basket
        btnRemove.setOnClickListener(v -> {
            sessionManager.removeFromBasket(product);
            Toast.makeText(ProductDetailsActivity.this, "Product removed from basket", Toast.LENGTH_SHORT).show();

            Intent mainActivityIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
            // https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_CLEAR_TOP
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainActivityIntent);
        });

        btnOrder.setOnClickListener(v -> {
            // Create a date string for the current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            // Add the order to the database and get its ID

            // For status, choose randomly between "shipped", "delivered", and "ordered", ...
            String[] statuses = {"shipped", "delivered", "ordered", "pending"};
            String status = statuses[(int) (Math.random() * statuses.length)];
            long orderId = sqLiteHelper.addOrder(user.getId(), currentDate, status);

            // Add the corresponding order item to the database (assuming a quantity of 1)
            sqLiteHelper.addOrderItem((int) orderId, product.getId(), 1);

            Toast.makeText(ProductDetailsActivity.this, "Order placed, click 'orders' to see it", Toast.LENGTH_LONG).show();

            Intent mainActivityIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainActivityIntent);
        });
    }

    private void deleteProduct(Product product) {
        // Assuming you have a method in your SQLiteHelper class to delete a product by its ID and a method to get the current product's ID
        sqLiteHelper.deleteProduct(product.getId());

        // Display a message to inform the user that the product has been deleted
        Toast.makeText(ProductDetailsActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();

        // Start the MainActivity to update recycler view
        Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void addToBasket(int productId) {
        // Get the user's current basket from SharedPreferences
        String basketString = sessionManager.getUserBasket();

        // Add the new product to the basket
        basketString = basketString.isEmpty() ? String.valueOf(productId) : basketString + "," + productId;

        // Save the updated basket in SharedPreferences
        sessionManager.saveUserBasket(basketString);

        Toast.makeText(ProductDetailsActivity.this, "Added to basket, click 'basket' to see it", Toast.LENGTH_LONG).show();

        Intent mainActivityIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivityIntent);
        finish();
    }
}