package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProductActivity extends AppCompatActivity {

    private EditText etProductName;
    private EditText etProductDescription;
    private EditText etProductPrice;
    private Button btnUpdateProduct;

    private SQLiteHelper sqLiteHelper;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        etProductName = findViewById(R.id.et_product_name);
        etProductDescription = findViewById(R.id.et_product_description);
        etProductPrice = findViewById(R.id.et_product_price);
        btnUpdateProduct = findViewById(R.id.btn_update_product);

        sqLiteHelper = new SQLiteHelper(this);

        // Retrieve the product ID from the Intent
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        // Get the product details from the database
        product = sqLiteHelper.getProductById(productId);

        // Set the input fields with the current product details
        etProductName.setText(product.getName());
        etProductDescription.setText(product.getDescription());
        etProductPrice.setText(String.valueOf(product.getPrice()));

        // Set a click listener for the update button
        btnUpdateProduct.setOnClickListener(v -> {
            // Get the updated product details from the input fields
            String updatedName = etProductName.getText().toString();
            String updatedDescription = etProductDescription.getText().toString();
            double updatedPrice = Double.parseDouble(etProductPrice.getText().toString());

            // Update the product details in the database
            sqLiteHelper.updateProduct(product.getId(), updatedName, updatedDescription, updatedPrice);

            // Intent intent = new Intent(EditProductActivity.this, ProductDetailsActivity.class);
            Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
            // https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            // intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
            finish();
        });
    }
}