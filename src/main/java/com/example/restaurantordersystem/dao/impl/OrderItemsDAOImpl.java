package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.OrderItemsDAO;
import com.example.restaurantordersystem.model.OrderItem;
import com.example.restaurantordersystem.model.MenuItem;
import com.example.restaurantordersystem.model.Category;
import com.example.restaurantordersystem.db.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsDAOImpl implements OrderItemsDAO {

    @Override
    public OrderItem findById(int id) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, order_id, menu_item_id, quantity FROM order_items WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                int menuItemId = rs.getInt("menu_item_id");
                int quantity = rs.getInt("quantity");

                // Assuming you have a method to fetch MenuItem by ID
                MenuItem menuItem = getMenuItemById(menuItemId, conn);

                return new OrderItem(id, orderId, menuItem, quantity);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error finding order item: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public List<OrderItem> findAll(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, order_id, menu_item_id, quantity FROM order_items WHERE order_id = ?");
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int menuItemId = rs.getInt("menu_item_id");
                int quantity = rs.getInt("quantity");

                // create a helper method to fetch MenuItem by ID
                MenuItem menuItem = getMenuItemById(menuItemId, conn);

                orderItems.add(new OrderItem(id, orderId, menuItem, quantity));
            }
        } catch (SQLException e) {
            System.err.println("Error finding order items: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return orderItems;
    }

    @Override
    public boolean save(OrderItem orderItem) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)");
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenuItem().getId());
            stmt.setInt(3, orderItem.getQuantity());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error saving order item: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean update(OrderItem orderItem) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE order_items SET order_id = ?, menu_item_id = ?, quantity = ? WHERE id = ?");
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenuItem().getId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setInt(4, orderItem.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order item: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean delete(int id) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM order_items WHERE id = ?");
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order item: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    // Helper method to fetch MenuItem by ID
    private MenuItem getMenuItemById(int menuItemId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT item_id, name, description, price, category, is_available FROM menu_items WHERE item_id = ?");
        stmt.setInt(1, menuItemId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("item_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            String categoryStr = rs.getString("category");
            boolean isAvailable = rs.getBoolean("is_available");

            Category category = Category.valueOf(categoryStr.toUpperCase());
            return new MenuItem(id, name, description, price, category, isAvailable);
        }
        return null;
    }
}