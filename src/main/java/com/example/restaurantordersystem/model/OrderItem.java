package com.example.restaurantordersystem.model;

public class OrderItem {
    private int id;
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(int id, MenuItem menuItem, int quantity) {
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
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
                ", menuItem=" + menuItem +
                ", quantity=" + quantity +
                '}';
    }
}
