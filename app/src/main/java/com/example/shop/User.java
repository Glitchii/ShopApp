package com.example.shop;

public class User {
    private final int id;
    private String fullName;
    private String email;
    private String password;
    private String hobbies;
    private String postcode;
    private int isAdmin;
    private String address;

    public User(int id, String fullName, String email, String password, String hobbies, String postcode, String address, int isAdmin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.hobbies = hobbies;
        this.postcode = postcode;
        this.address = address;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }
    public int getIsAdmin() {
        return isAdmin;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getHobbies() {
        return hobbies;
    }

    public String getPostcode() {
        return postcode;
    }
    public String getAddress() {
        return address;
    }
}