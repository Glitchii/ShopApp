package com.example.shop;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class ProductsFragment extends Fragment {

    private RecyclerView rvItems;
    private SQLiteHelper sqLiteHelper;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        rvItems = view.findViewById(R.id.rv_items);

        sqLiteHelper = new SQLiteHelper(getContext());

        // Fetch the list of products
        List<Product> products = sqLiteHelper.getAllProducts();

        // Create and set the adapter for the RecyclerView
        DashboardItemAdapter dashboardItemAdapter = new DashboardItemAdapter(getContext(), products);
        rvItems.setAdapter(dashboardItemAdapter);

        // Set the layout manager for the RecyclerView
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}