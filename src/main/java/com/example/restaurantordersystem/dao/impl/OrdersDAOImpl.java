package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.OrdersDAO;
import com.example.restaurantordersystem.model.Order;
import com.example.restaurantordersystem.db.DbUtil;
import com.example.restaurantordersystem.model.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAOImpl implements OrdersDAO {
    @Override
    public Order findById(int orderId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT order_id, table_number, status, order_time, completion_time, is_paid, total_amount "
                            + "FROM orders WHERE order_id = ?");
            stmt.setInt(1, orderId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Order order = new Order(rs.getInt("order_id"), rs.getInt("table_number"));
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));

                Timestamp ot = rs.getTimestamp("order_time");
                if (ot != null) {
                    order.setOrderTime(ot.toLocalDateTime());
                }

                Timestamp ct = rs.getTimestamp("completion_time");
                if (ct != null) {
                    order.setCompletionTime(ct.toLocalDateTime());
                }

                order.setPaid(rs.getBoolean("is_paid"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                return order;
            } else {
                System.out.println("Order not found with ID: " + orderId);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error finding order: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT order_id, table_number, status, order_time, completion_time, is_paid, total_amount FROM orders"
            );

            while (rs.next()) {
                Order order = new Order(rs.getInt("order_id"), rs.getInt("table_number"));
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));

                Timestamp ot = rs.getTimestamp("order_time");
                if (ot != null) {
                    order.setOrderTime(ot.toLocalDateTime());
                }

                Timestamp ct = rs.getTimestamp("completion_time");
                if (ct != null) {
                    order.setCompletionTime(ct.toLocalDateTime());
                }

                order.setPaid(rs.getBoolean("is_paid"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all orders: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return orders;
    }

    @Override
    public boolean save(Order order) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM orders WHERE order_id = ?");
            checkStmt.setInt(1, order.getOrderId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count > 0) {
                // Update existing order
                stmt = conn.prepareStatement(
                        "UPDATE orders "
                                + "SET table_number = ?, status = ?, order_time = ?, "
                                + "    is_paid = ?, total_amount = ? "
                                + "WHERE order_id = ?"
                );
                stmt.setInt(1, order.getTableNumber());
                stmt.setString(2, order.getStatus().name());
                stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderTime()));
                stmt.setBoolean(4, order.isPaid());
                stmt.setDouble(5, order.getTotalAmount());
                stmt.setInt(6, order.getOrderId());
            } else {
                // Insert new order
                stmt = conn.prepareStatement(
                        "INSERT INTO orders (table_number, status, order_time, is_paid, total_amount) "
                                + "VALUES (?,?,?,?,?)"
                );
                stmt.setInt(1, order.getTableNumber());
                stmt.setString(2, order.getStatus().name());
                stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderTime()));
                stmt.setBoolean(4, order.isPaid());
                stmt.setDouble(5, order.getTotalAmount());
            }

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saving order: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean delete(int orderId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM orders WHERE order_id = ?");
            stmt.setInt(1, orderId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
