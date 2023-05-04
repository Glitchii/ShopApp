package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashboardItemAdapter extends RecyclerView.Adapter<DashboardItemAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> products;
    private boolean fromBasketActivity;

    /**
     * Constructs a new DashboardItemAdapter with the specified context, list of products, and a flag indicating
     * if the adapter is being used in the BasketActivity.
     *
     * @param context           The context in which the adapter is running.
     * @param products          The list of products to be displayed.
     * @param fromBasketActivity A boolean flag indicating if the adapter is being used in the BasketActivity.
     */
    public DashboardItemAdapter(Context context, List<Product> products, boolean fromBasketActivity) {
        this.context = context;
        this.products = products;
        this.fromBasketActivity = fromBasketActivity;
    }

    /**
     * Constructs a new DashboardItemAdapter with the specified context and list of products.
     * Assumes that the adapter is not being used in the BasketActivity.
     *
     * @param context  The context in which the adapter is running.
     * @param products The list of products to be displayed.
     */
    public DashboardItemAdapter(Context context, List<Product> products) {
        this(context, products, false);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductDescription.setText(product.getDescription());
        holder.tvProductPrice.setText(String.format("Price: £%.2f", product.getPrice()));

        // Set an onClickListener for the itemView
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            // Pass the product ID or the entire Product object
            intent.putExtra("PRODUCT_ID", product.getId());
            intent.putExtra("FROM_BASKET_ACTIVITY", fromBasketActivity);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        private TextView tvProductDescription;
        private TextView tvProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.item_title);
            tvProductDescription = itemView.findViewById(R.id.item_description);
            tvProductPrice = itemView.findViewById(R.id.item_price);
        }
    }
}
