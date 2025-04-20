package com.example.restaurantordersystem.db;

import java.sql.*;
import java.time.LocalDateTime;

public class DbTestMain {

    public static void main(String[] args) {
        // 1. Initialize schema and insert sample data
        SchemaInitializer.initializeSchema();
        SchemaInitializer.insertSampleData();

        // 2. Insert test user
        insertTestUser();

        // 3. Create a test order with items
        createTestOrder();

        // 4. Process a test payment
        processTestPayment();

        // 5. Verify all test data
        verifyTestData();
    }

    private static void insertTestUser() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Check if test user already exists
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE email = ?");
            checkStmt.setString(1, "test@restaurant.com");
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                // Insert test user
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)");
                stmt.setString(1, "Test User");
                stmt.setString(2, "test@restaurant.com");
                stmt.setString(3, "password123");
                stmt.setString(4, "Customer");
                stmt.executeUpdate();

                System.out.println("Test user inserted.");
            } else {
                System.out.println("Test user already exists.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting test user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    private static void createTestOrder() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // 1. Create a test order
            PreparedStatement orderStmt = conn.prepareStatement(
                    "INSERT INTO orders (table_number, status, order_time, total_amount) " +
                            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, 3); // Table 3
            orderStmt.setString(2, "PLACED");
            orderStmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            orderStmt.setDouble(4, 0.0); // Initial total
            orderStmt.executeUpdate();

            // Get the generated order ID
            ResultSet keys = orderStmt.getGeneratedKeys();
            int orderId;
            if (keys.next()) {
                orderId = keys.getInt(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            // 2. Add items to the order
            // First, get some menu items
            PreparedStatement menuStmt = conn.prepareStatement(
                    "SELECT item_id, price FROM menu_items LIMIT 2");
            ResultSet menuItems = menuStmt.executeQuery();

            double totalAmount = 0;
            while (menuItems.next()) {
                int itemId = menuItems.getInt("item_id");
                double itemPrice = menuItems.getDouble("price");
                int quantity = 2; // Order 2 of each item

                // Add order item
                PreparedStatement itemStmt = conn.prepareStatement(
                        "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)");
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, itemId);
                itemStmt.setInt(3, quantity);
                itemStmt.executeUpdate();

                totalAmount += (itemPrice * quantity);
            }

            // 3. Update order total
            PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE orders SET total_amount = ? WHERE order_id = ?");
            updateStmt.setDouble(1, totalAmount);
            updateStmt.setInt(2, orderId);
            updateStmt.executeUpdate();

            System.out.println("Test order created with ID: " + orderId + " and total: $" + totalAmount);

        } catch (SQLException e) {
            System.err.println("Error creating test order: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    private static void processTestPayment() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Get the latest order
            PreparedStatement orderStmt = conn.prepareStatement(
                    "SELECT order_id, total_amount FROM orders ORDER BY order_id DESC LIMIT 1");
            ResultSet orderRs = orderStmt.executeQuery();

            if (orderRs.next()) {
                int orderId = orderRs.getInt("order_id");
                double amount = orderRs.getDouble("total_amount");

                // Process payment
                PreparedStatement paymentStmt = conn.prepareStatement(
                        "INSERT INTO payments (order_id, payment_status, payment_method, amount_paid, payment_time) " +
                                "VALUES (?, ?, ?, ?, ?)");
                paymentStmt.setInt(1, orderId);
                paymentStmt.setString(2, "COMPLETED");
                paymentStmt.setString(3, "CREDIT_CARD");
                paymentStmt.setDouble(4, amount);
                paymentStmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                paymentStmt.executeUpdate();

                // Mark order as paid
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE orders SET is_paid = true WHERE order_id = ?");
                updateStmt.setInt(1, orderId);
                updateStmt.executeUpdate();

                System.out.println("Payment processed for order: " + orderId);
            }

        } catch (SQLException e) {
            System.err.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    private static void verifyTestData() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // 1. Verify test user
            PreparedStatement userStmt = conn.prepareStatement(
                    "SELECT * FROM users WHERE email = 'test@restaurant.com'");
            ResultSet userRs = userStmt.executeQuery();
            if (userRs.next()) {
                System.out.println("Test user verified: " + userRs.getString("full_name"));
            } else {
                System.out.println("Test user not found!");
            }

            // 2. Verify test order and payment
            PreparedStatement orderStmt = conn.prepareStatement(
                    "SELECT o.order_id, o.total_amount, o.is_paid, " +
                            "COUNT(oi.id) AS item_count, p.payment_method " +
                            "FROM orders o " +
                            "JOIN order_items oi ON o.order_id = oi.order_id " +
                            "LEFT JOIN payments p ON o.order_id = p.order_id " +
                            "GROUP BY o.order_id, o.total_amount, o.is_paid, p.payment_method " +
                            "ORDER BY o.order_id DESC LIMIT 1");

            ResultSet orderRs = orderStmt.executeQuery();
            if (orderRs.next()) {
                System.out.println("\nTest Order Summary:");
                System.out.println("Order ID: " + orderRs.getInt("order_id"));
                System.out.println("Total Amount: $" + orderRs.getDouble("total_amount"));
                System.out.println("Item Count: " + orderRs.getInt("item_count"));
                System.out.println("Payment Method: " + orderRs.getString("payment_method"));
                System.out.println("Is Paid: " + orderRs.getBoolean("is_paid"));
            } else {
                System.out.println("Test order not found!");
            }

        } catch (SQLException e) {
            System.err.println("Error verifying test data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}