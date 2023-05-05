// TODO: Remove before submitting
// Docs to used:
//      - https://developer.android.com/reference/android/arch/persistence/room/package-summary
//      - https://developer.android.com/reference/android/arch/persistence/room/RawQuery
//      - https://www.digitalocean.com/community/tutorials/android-sqlite-database-example-tutorial


package com.example.shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                    "date_created TEXT DEFAULT CURRENT_TIMESTAMP," +
                    "date_updated TEXT DEFAULT CURRENT_TIMESTAMP," +
                    "password TEXT NOT NULL," +
                    "hobbies TEXT," +
                    "postcode TEXT," +
                    "address TEXT," +
                    "is_admin INTEGER DEFAULT 0" +
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
                    "date_updated TEXT DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (category_id) REFERENCES categories (id)" +
                ");";

        db.execSQL(createProductsTable);

        String createOrdersTable =
                "CREATE TABLE orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "status TEXT NOT NULL," +
                    "date_created TEXT DEFAULT CURRENT_TIMESTAMP," +
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

    public long addUser(String fullName, String email, String password, String hobbies, String postcode, String address, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("full_name", fullName);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("hobbies", hobbies);
        contentValues.put("postcode", postcode);
        contentValues.put("address", address);
        contentValues.put("is_admin", isAdmin ? 1 : 0);

        return db.insert("users", null, contentValues);
    }

    public long addUser(String fullName, String email, String password, String hobbies, String postcode, String address) {
        return addUser(fullName, email, password, hobbies, postcode, address, false);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hobbies")),
                        cursor.getString(cursor.getColumnIndexOrThrow("postcode")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_admin"))
                );
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    /**
     * Retrieves a User object containing all user details for a given email address.
     *
     * @param email The email address of the user to be retrieved.
     * @return A User object containing all the user details if the email address exists in the database, otherwise, returns null.
     */
    public User getUserByEmail(String email) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("hobbies")),
                    cursor.getString(cursor.getColumnIndexOrThrow("postcode")),
                    cursor.getString(cursor.getColumnIndexOrThrow("address")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_admin"))
            );
        }

        cursor.close();
        db.close();
        return user;
    }

    /**
     * Checks whether the user with the given email and password exists in the database.
     *
     * @param email the email address of the user to be authenticated
     * @param password the password of the user to be authenticated
     * @return true if the user exists, false otherwise
     */
    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    /**
     * Fetches all products from the database and returns them as a list of Product objects.
     *
     * @return List of Product objects containing all products in the database.
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("list_price")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("retail_price"))
                );
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    /**
     * Adds a new product to the database with the given details.
     *
     * @param categoryId The category ID of the product.
     * @param name The name of the product.
     * @param description The description of the product.
     * @param price The price of the product.
     * @param listPrice The list price of the product.
     * @param retailPrice The retail price of the product.
     * @return The row ID of the newly inserted product, or -1 if an error occurred.
     */
    public long addProduct(int categoryId, String name, String description, double price, double listPrice, double retailPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category_id", categoryId);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("price", price);
        contentValues.put("list_price", listPrice);
        contentValues.put("retail_price", retailPrice);
        long newRowId = db.insert("products", null, contentValues);
        db.close();
        return newRowId;
    }

    /**
     * Retrieves a Product object containing all product details for a given product ID.
     * @param id The ID of the product to be retrieved.
     * @return A Product object containing all the product details if the product ID exists in the database, otherwise, returns null.
     */
    public Product getProductById(int id) {
        Product product = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("list_price")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("retail_price"))
            );
        }

        cursor.close();
        db.close();
        return product;
    }

    public List<Product> getProductsByIds(List<String> ids) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
    
        for (String id : ids) {
            Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{id});

            if (cursor.moveToFirst()) {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("list_price")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("retail_price"))
                );
                products.add(product);
            }

            cursor.close();
        }

        db.close();
        return products;
    }
    

    /**
     * Deletes a product from the database with the given ID.
     * @param id The ID of the product to be deleted.
     * @return The number of rows affected if a product was deleted, otherwise, returns 0.
     */
    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("products", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    /**
     * Updates the details of a product with the given ID.
     * @param id The ID of the product to be updated.
     * @param name The updated name of the product.
     * @param description The updated description of the product.
     * @param price The updated price of the product.
     * @param listPrice The updated list price of the product.
     * @param retailPrice The updated retail price of the product.
     * @return The number of rows affected if a product was updated, otherwise, returns 0.
     */
    public int updateProduct(int id, String name, String description, double price, double listPrice, double retailPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("price", price);
        contentValues.put("list_price", listPrice);
        contentValues.put("retail_price", retailPrice);

        // Update the date_updated column with the current timestamp
        contentValues.put("date_updated", getCurrentTimestamp());

        int rowsAffected = db.update("products", contentValues, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    private String getCurrentTimestamp() {
        // https://stackoverflow.com/a/23068721
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Fetches all categories from the database and returns them as a list of Category objects.
     *
     * @return List of Category objects containing all categories in the database.
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categories", null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name"))
                );
                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categories;
    }

    /**
     * Adds a new category to the database with the given details.
     *
     * @param name The name of the category.
     * @return The row ID of the newly inserted category, or -1 if an error occurred.
     */
    public long addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        long newRowId = db.insert("categories", null, contentValues);
        db.close();
        return newRowId;
    }
    
    /**
     * Retrieves a Category object containing all category details for a given category ID.
     * @param id The ID of the category to be retrieved.
     * @return A Category object containing all the category details if the category ID exists in the database, otherwise, returns null.
     */
    public Category getCategoryById(int id) {
        Category category = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categories WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            category = new Category(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name"))
            );
        }

        cursor.close();
        db.close();
        return category;
    }

    /**
     * Deletes a category from the database with the given ID.
     * @param id The ID of the category to be deleted.
     * @return boolean indicating whether the category was deleted or not.
     */
    public boolean deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("categories", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    /**
     * Updates the details of a category with the given ID.
     * @param id The ID of the category to be updated.
     * @param name The updated name of the category.
     * @return The number of rows affected if a category was updated, otherwise, returns 0.
     */
    public int updateCategory(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        int rowsAffected = db.update("categories", contentValues, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public List<Order> getAllOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE user_id = ?", new String[]{String.valueOf(userId)});
    
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date_created"))
                );
                orders.add(order);
            } while (cursor.moveToNext());
        }
    
        cursor.close();
        db.close();
        return orders;
    }
    

    /**
     * Fetches all orders from the database and returns them as a list of Order objects.
     *
     * @return List of Order objects containing all orders in the database.
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders", null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date_created"))
                );
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }

    /**
     * Adds a new order to the database with the given details.
     *
     * @param userId The user ID of the order.
     * @param date The date of the order.
     * @param status The status of the order.
     * @return The row ID of the newly inserted order, or -1 if an error occurred.
     */
    public long addOrder(int userId, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("date_created", date);
        contentValues.put("status", status);
        long newRowId = db.insert("orders", null, contentValues);
        db.close();
        return newRowId;
    }

    public int deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("orders", "id = ?", new String[]{String.valueOf(orderId)});
        db.close();
        return rowsAffected;
    }

    public int updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        int rowsAffected = db.update("orders", contentValues, "id = ?", new String[]{String.valueOf(orderId)});
        db.close();
        return rowsAffected;
    }

    /**
     * Adds a new order item to the database with the given details.
     *
     * @param orderId The order ID of the order item.
     * @param productId The product ID of the order item.
     * @param quantity The quantity of the order item.
     * @return The row ID of the newly inserted order item, or -1 if an error occurred.
     */
    public long addOrderItem(int orderId, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_id", orderId);
        contentValues.put("product_id", productId);
        contentValues.put("quantity", quantity);
        long newRowId = db.insert("order_items", null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Order> getOrdersByUser(int userId, boolean isAdmin) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> orders = new ArrayList<>();

        String selection = isAdmin ? null : "user_id = ?";
        String[] selectionArgs = isAdmin ? null : new String[] { String.valueOf(userId) };
        Cursor cursor = db.query("orders", null, selection, selectionArgs, null, null, "date_created DESC");

        if (cursor.moveToFirst())
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String date_created = cursor.getString(cursor.getColumnIndexOrThrow("date_created"));

                Order order = new Order(id, user_id, status, date_created);
                orders.add(order);

            } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return orders;
    }
}