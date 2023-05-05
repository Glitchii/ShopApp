package com.example.shop;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private EditText etProductName;
    private EditText etProductDescription;
    private EditText etProductPrice;
    private Spinner spinnerCategory;
    private Button btnAddProduct;
    private SQLiteHelper sqLiteHelper;
    private EditText etListPrice;
    private EditText etRetailPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etProductName = findViewById(R.id.et_product_name);
        etProductDescription = findViewById(R.id.et_product_description);
        etProductPrice = findViewById(R.id.et_product_price);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnAddProduct = findViewById(R.id.btn_add_product);
        etListPrice = findViewById(R.id.et_list_price);
        etRetailPrice = findViewById(R.id.et_retail_price);

        sqLiteHelper = new SQLiteHelper(this);

        populateCategorySpinner();

        btnAddProduct.setOnClickListener(v -> addProduct());
    }

    private void populateCategorySpinner() {
        // https://developer.android.com/develop/ui/views/components/spinner
        List<Category> categories = sqLiteHelper.getAllCategories();

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        if (categories.isEmpty())
            Toast.makeText(this, "There are no categories to select, add a category first", Toast.LENGTH_LONG).show();
    }

    private void addProduct() {
        String name = etProductName.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();
        String priceString = etProductPrice.getText().toString().trim();
        String listPriceString = etListPrice.getText().toString().trim();
        String retailPriceString = etRetailPrice.getText().toString().trim();
        Category category = (Category) spinnerCategory.getSelectedItem();

        if (name.isEmpty() || description.isEmpty() || priceString.isEmpty() || listPriceString.isEmpty() || retailPriceString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category == null) {
            Toast.makeText(this, "No category available. Please add a category first.", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);
        double listPrice = Double.parseDouble(listPriceString);
        double retailPrice = Double.parseDouble(retailPriceString);

        long newProductId = sqLiteHelper.addProduct(category.getId(), name, description, price, listPrice, retailPrice);

        if (newProductId != -1) {
            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
        }
    }
}