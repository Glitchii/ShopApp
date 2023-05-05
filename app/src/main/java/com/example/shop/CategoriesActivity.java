package com.example.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements CategoriesAdapter.OnCategoryClickListener {

    private RecyclerView rvCategories;
    private FloatingActionButton fabAddCategory;
    private SQLiteHelper sqLiteHelper;
    private boolean categoriesFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        rvCategories = findViewById(R.id.rv_categories);
        fabAddCategory = findViewById(R.id.fab_add_category);
        sqLiteHelper = new SQLiteHelper(this);

        setupCategories();

        if (categoriesFound)
            Toast.makeText(this, "Click category above to update/delete", Toast.LENGTH_LONG).show();

        fabAddCategory.setOnClickListener(v -> showAddCategoryDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCategories();
    }

    private void setupCategories() {
        List<Category> categories = sqLiteHelper.getAllCategories();
        if (categories.isEmpty()) {
            Toast.makeText(this, "No categories found", Toast.LENGTH_SHORT).show();
            return;
        }

        categoriesFound = true;
        CategoriesAdapter adapter = new CategoriesAdapter(this, categories, this);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(adapter);
    }

    public void onCategoryClick(int position) {
        // Open a new activity to manage the category with the ID of the clicked category
        Category category = sqLiteHelper.getAllCategories().get(position);
        Intent intent = new Intent(this, ManageCategoryActivity.class);
        intent.putExtra("CATEGORY_ID", category.getId());
        startActivity(intent);
    }

    private void showAddCategoryDialog() {
        // https://developer.android.com/develop/ui/views/components/dialogs
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Category");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        final EditText etCategoryName = view.findViewById(R.id.et_category_name);

        builder.setView(view);
        builder.setPositiveButton("Add", (DialogInterface dialog, int which) -> {
            String categoryName = etCategoryName.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                sqLiteHelper.addCategory(categoryName);
                setupCategories(); // Refresh the category list
            } else {
                Toast.makeText(CategoriesActivity.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (DialogInterface dialog, int which) -> dialog.dismiss());
        builder.create().show();
    }
}