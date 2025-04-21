package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.PaymentsDAO;
import com.example.restaurantordersystem.db.DbUtil;
import com.example.restaurantordersystem.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentsDAOImpl implements PaymentsDAO {

    @Override
    public boolean savePayment(Payment payment) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO payments (order_id, payment_status, payment_method, amount_paid, payment_time) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setInt(1, payment.getOrder().getOrderId());
            stmt.setString(2, payment.getPaymentStatus().name());
            stmt.setString(3, payment.getPaymentMethod().name());
            stmt.setDouble(4, payment.getAmountPaid());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentTime()));

            int result = stmt.executeUpdate();

            if (result > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    payment.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error saving payment: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public Payment findPaymentByID(int PaymentId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM payments WHERE id = ?");
            stmt.setInt(1, PaymentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                PaymentStatus status = PaymentStatus.valueOf(rs.getString("payment_status"));
                PaymentMethod method = PaymentMethod.valueOf(rs.getString("payment_method"));
                double amount = rs.getDouble("amount_paid");
                Timestamp timestamp = rs.getTimestamp("payment_time");

                Order order = new Order(); // You'll need to fetch actual order if required
                order.setOrderId(orderId);

                Payment payment = new Payment(PaymentId, order, method);
                payment.processPayment();
                return payment;
            }

        } catch (SQLException e) {
            System.err.println("Error finding payment: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return null;
    }

    @Override
    public List<Payment> findAllPayments() {
        List<Payment> payments = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM payments");

            while (rs.next()) {
                int id = rs.getInt("id");
                int orderId = rs.getInt("order_id");
                PaymentStatus status = PaymentStatus.valueOf(rs.getString("payment_status"));
                PaymentMethod method = PaymentMethod.valueOf(rs.getString("payment_method"));
                double amount = rs.getDouble("amount_paid");
                Timestamp time = rs.getTimestamp("payment_time");

                Order order = new Order(); // finish orderDAO first
                order.setOrderId(orderId);

                Payment payment = new Payment(id, order, method);
                payment.processPayment();
                payments.add(payment);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all payments: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }

        return payments;
    }

    @Override
    public boolean deletePayment(int id) {
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM payments WHERE id = ?");
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
