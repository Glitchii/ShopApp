package com.example.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ManageCategoryActivity extends AppCompatActivity {

    private TextView tvCategoryName;
    private Button btnDeleteCategory;
    private SQLiteHelper sqLiteHelper;
    private int categoryId;
    private Button btnUpdateCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        tvCategoryName = findViewById(R.id.tv_category_name);
        btnDeleteCategory = findViewById(R.id.btn_delete_category);
        btnUpdateCategory = findViewById(R.id.btn_update_category);

        sqLiteHelper = new SQLiteHelper(this);
        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        if (categoryId == -1) {
            finish();
            return;
        }

        Category category = sqLiteHelper.getCategoryById(categoryId);
        SessionManager sessionManager = new SessionManager(this);
        User user = sessionManager.getUserDetails(this);

        if (category != null)
            tvCategoryName.setText(category.getName());
        else {
            finish();
            return;
        }

        btnDeleteCategory.setOnClickListener(v -> deleteCategory(user));
        btnUpdateCategory.setOnClickListener(v -> showUpdateCategoryDialog(user));
    }

    private void deleteCategory(User user) {
        if (!user.isAdmin()) {
            Toast.makeText(this, "Admin action only. Register or login as admin", Toast.LENGTH_LONG).show();
            return;
        }

        boolean success = sqLiteHelper.deleteCategory(categoryId);

        if (success) {
            Toast.makeText(this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CategoriesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to delete category", Toast.LENGTH_SHORT).show();
        }
    }


    private void showUpdateCategoryDialog(User user) {
        if (!user.isAdmin()) {
            Toast.makeText(this, "Admin action only. Register or login as admin", Toast.LENGTH_LONG).show();
            return;
        }

        // https://developer.android.com/develop/ui/views/components/dialogs
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Category");

        View view = getLayoutInflater().inflate(R.layout.dialog_update_category, null);
        final EditText etCategoryName = view.findViewById(R.id.et_category_name);

        Category category = sqLiteHelper.getCategoryById(categoryId);
        if (category != null) {
            etCategoryName.setText(category.getName());
        } else {
            Toast.makeText(this, "Category not found", Toast.LENGTH_SHORT).show();
            return;
        }

        builder.setView(view);
        builder.setPositiveButton("Update", (DialogInterface dialog, int which) -> {
            String categoryName = etCategoryName.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                sqLiteHelper.updateCategory(category.getId(), categoryName);
                Toast.makeText(this, "Updated category", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ManageCategoryActivity.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (DialogInterface dialog, int which) -> dialog.dismiss());
        builder.create().show();
    }
}