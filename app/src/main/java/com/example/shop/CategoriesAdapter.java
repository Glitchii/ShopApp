package com.example.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categories;
    private OnCategoryClickListener onCategoryClickListener;

    public CategoriesAdapter(Context context, List<Category> categories, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categories = categories;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view, onCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCategoryName;
        OnCategoryClickListener onCategoryClickListener;

        public CategoryViewHolder(@NonNull View itemView, OnCategoryClickListener onCategoryClickListener) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            this.onCategoryClickListener = onCategoryClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCategoryClickListener.onCategoryClick(getAdapterPosition());
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }
}