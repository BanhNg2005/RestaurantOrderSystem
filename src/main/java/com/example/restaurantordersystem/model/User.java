package com.example.restaurantordersystem.model;

import java.util.List;

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String role = "Customer"; // Default role, will add 2 login for customer and admin(where admin can create new item like food or even table)
    private List<Order> orders;
    private List<Reservation> reservations;

    // Constructors
    public User() {}

    public User(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(int userId, String username, String password, String email, String role) {
        this.userId = userId;
        this.fullName = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }


    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}