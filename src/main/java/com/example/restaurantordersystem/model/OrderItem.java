package com.example.restaurantordersystem.model;

public class OrderItem {
    private int id;
    private int orderId;
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(int id, int orderId, MenuItem menuItem, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", menuItem=" + menuItem +
                ", quantity=" + quantity +
                '}';
    }
}