package com.example.restaurantordersystem.model;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private double price;
    private Category category;
    private boolean isAvailable;

    public MenuItem(int id, String name, String description, double price, Category category, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
