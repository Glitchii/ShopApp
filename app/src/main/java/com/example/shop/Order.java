package com.example.shop;

public class Order {
    private int id;
    private int userId;
    private String status;
    private String dateCreated;

    public Order(int id, int userId, String status, String dateCreated) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}