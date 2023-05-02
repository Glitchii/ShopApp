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

import java.util.ArrayList;
import java.util.List;

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

    public long addUser(String fullName, String email, String password, String hobbies, String postcode, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("full_name", fullName);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("hobbies", hobbies);
        contentValues.put("postcode", postcode);
        contentValues.put("address", address);
        long newRowId = db.insert("users", null, contentValues);
        db.close();
        return newRowId;
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
                        cursor.getString(cursor.getColumnIndexOrThrow("address"))
                );
                users.add(user);
            } while (cursor.moveToNext());
        }
    
        cursor.close();
        db.close();
        return users;
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
}