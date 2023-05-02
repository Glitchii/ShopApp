package com.example.shop;

public class User {
    private final int id;
    private String fullName;
    private String email;
    private String password;
    private String hobbies;
    private String postcode;
    private String address;

    public User(int id, String fullName, String email, String password, String hobbies, String postcode, String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.hobbies = hobbies;
        this.postcode = postcode;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}