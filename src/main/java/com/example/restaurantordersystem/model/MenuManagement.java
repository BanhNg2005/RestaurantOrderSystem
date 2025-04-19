package com.example.restaurantordersystem.model;

import java.util.ArrayList;
import java.util.List;

public class MenuManagement {
    public List<MenuItem> menuItems;

    public MenuManagement() {
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(MenuItem menuItem) {
        try {
            if (menuItem != null) {
                menuItems.add(menuItem);
            } else {
                throw new IllegalArgumentException("Menu item cannot be null");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeMenuItemByID(int id) {
        try {
            MenuItem itemToRemove = null;
            for (MenuItem item : menuItems) {
                if (item.getId() == id) {
                    itemToRemove = item;
                    break;
                }
            }
            if (itemToRemove != null) {
                menuItems.remove(itemToRemove);
            } else {
                throw new IllegalArgumentException("Menu item with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
