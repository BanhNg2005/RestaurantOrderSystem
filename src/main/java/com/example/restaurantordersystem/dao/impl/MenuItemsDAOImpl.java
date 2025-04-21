package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.MenuItemsDAO;
import com.example.restaurantordersystem.db.DbUtil;
import com.example.restaurantordersystem.model.Category;
import com.example.restaurantordersystem.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemsDAOImpl implements MenuItemsDAO {
    @Override
    public MenuItem findMenuItemById(int id) {
        Connection conn = null;
        MenuItem menuItem = null;

        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM menu_items WHERE item_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                Category category = Category.valueOf(rs.getString("category"));
                boolean isAvailable = rs.getBoolean("is_available");

                menuItem = new MenuItem(id, name, description, price, category, isAvailable);
            }
        } catch (SQLException e) {
            System.err.println("Error finding menu item: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }

        return menuItem;
    }
    @Override
    public List<MenuItem> findAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items");

            while (rs.next()) {
                int id = rs.getInt("item_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                Category category = Category.valueOf(rs.getString("category"));
                boolean isAvailable = rs.getBoolean("is_available");

                items.add(new MenuItem(id, name, description, price, category, isAvailable));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all menu items: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }

        return items;
    }
    @Override
    public boolean updateMenuItem(MenuItem item) {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DbUtil.getConnection();

            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM menu_items WHERE item_id = ?");
            checkStmt.setInt(1, item.getId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count > 0) {
                stmt = conn.prepareStatement("UPDATE menu_items SET name = ?, description = ?, price = ?, category = ?, is_available = ? WHERE item_id = ?");
                stmt.setString(1, item.getName());
                stmt.setString(2, item.getDescription());
                stmt.setDouble(3, item.getPrice());
                stmt.setString(4, item.getCategory().name());
                stmt.setBoolean(5, item.isAvailable());
                stmt.setInt(6, item.getId());
            } else {
                stmt = conn.prepareStatement("INSERT INTO menu_items (name, description, price, category, is_available) VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, item.getName());
                stmt.setString(2, item.getDescription());
                stmt.setDouble(3, item.getPrice());
                stmt.setString(4, item.getCategory().name());
                stmt.setBoolean(5, item.isAvailable());
            }

            int result = stmt.executeUpdate();
            success = result > 0;

        } catch (SQLException e) {
            System.err.println("Error saving menu item: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }

        return success;
    }

    @Override
    public boolean deleteMenuItemById(int id) {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM menu_items WHERE item_id = ?");
            stmt.setInt(1, id);
            success = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting menu item: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }

        return success;
    }

    @Override
    public MenuItem mapResultSetToMenuItem(ResultSet rs) {
        try {
            int id = rs.getInt("item_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            String categoryStr = rs.getString("category");
            boolean isAvailable = rs.getBoolean("is_available");

            Category category = Category.valueOf(categoryStr); // assuming enum values match the DB values

            return new MenuItem(id, name, description, price, category, isAvailable);
        } catch (SQLException e) {
            System.err.println("Error mapping ResultSet to MenuItem: " + e.getMessage());
            return null;
        }
    }
}
