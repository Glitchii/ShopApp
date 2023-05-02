package com.example.shop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shop.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable =
                "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "full_name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "date_registered TEXT NOT NULL," +
                    "date_updated TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "hobbies TEXT," +
                    "postcode TEXT," +
                    "address TEXT" +
                ");";
        db.execSQL(createUsersTable);

        String createCategoriesTable =
                "CREATE TABLE categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL" +
                ");";
        db.execSQL(createCategoriesTable);

        String createProductsTable =
                "CREATE TABLE products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "category_id INTEGER," +
                    "name TEXT NOT NULL," +
                    "description TEXT," +
                    "price REAL NOT NULL," +
                    "list_price REAL," +
                    "retail_price REAL," +
                    "date_created TEXT DEFAULT CURRENT_TIMESTAMP," +
                    "date_updated TEXT," +
                    "FOREIGN KEY (category_id) REFERENCES categories (id)" +
                ");";

        db.execSQL(createProductsTable);

        String createOrdersTable =
                "CREATE TABLE orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "status TEXT NOT NULL," +
                    "date_created TEXT NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users (id)" +
                ");";
        db.execSQL(createOrdersTable);

        String createOrderItemsTable =
                "CREATE TABLE order_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER NOT NULL," +
                    "product_id INTEGER NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "FOREIGN KEY (order_id) REFERENCES orders (id)," +
                    "FOREIGN KEY (product_id) REFERENCES products (id)" +
                ");";
        db.execSQL(createOrderItemsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS order_items");
        onCreate(db);
    }
}