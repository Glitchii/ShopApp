package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView tvProductName;
    private TextView tvProductDescription;
    private TextView tvProductPrice;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnAddToBasket;

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
        

        // Initialize SQLiteHelper and SessionManager
        sqLiteHelper = new SQLiteHelper(this);
        sessionManager = new SessionManager(this);

        // Get the passed product ID from the intent
        Intent intent = getIntent();
        int productId = intent.getIntExtra("PRODUCT_ID", 0);

        // Fetch the product from the database
        Product product = sqLiteHelper.getProductById(productId);

        // Set the product information in the UI elements
        tvProductName.setText(product.getName());
        tvProductDescription.setText(product.getDescription());
        tvProductPrice.setText(String.format("£%.2f", product.getPrice()));

        // Set a click listener for the add to basket button
        btnAddToBasket.setOnClickListener(v -> addToBasket(product.getId()));

        // Check if the user is an admin
        String userEmail = sessionManager.getUserEmail();
        User user = sqLiteHelper.getUserByEmail(userEmail);
        // boolean isAdmin = user.getIsAdmin() == 1;
        boolean isAdmin = true; // debug

        // Show or hide the edit and delete buttons based on the user role
        if (isAdmin) {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

            // Implement the edit and delete functionality
            btnEdit.setOnClickListener(v -> {
                Intent editIntent = new Intent(ProductDetailsActivity.this, EditProductActivity.class);
                editIntent.putExtra("PRODUCT_ID", product.getId());
                startActivity(editIntent);
            });

            btnDelete.setOnClickListener(v -> deleteProduct(product));
        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void deleteProduct(Product product) {
        // Assuming you have a method in your SQLiteHelper class to delete a product by
        // its ID
        // and a method to get the current product's ID
        sqLiteHelper.deleteProduct(product.getId());

        // Display a message to inform the user that the product has been deleted
        Toast.makeText(ProductDetailsActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();

        // Start the MainActivity to update recycler view
        Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
        // https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Close the current activity
        finish();
    }

    private void addToBasket(int productId) {
        // Get the user's current basket from SharedPreferences
        String basketString = sessionManager.getUserBasket();
    
        // Check if the basket is empty
        if (basketString.isEmpty()) {
            basketString = String.valueOf(productId);
        } else {
            // If not empty, add the product ID to the basket
            basketString += "," + productId;
        }
    
        // Save the updated basket in SharedPreferences
        sessionManager.saveUserBasket(basketString);
    
        // Show a message to inform the user that the product has been added to the basket
        Toast.makeText(ProductDetailsActivity.this, "Product added to basket", Toast.LENGTH_SHORT).show();
    }
    
}