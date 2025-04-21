package com.example.restaurantordersystem.dao;

import com.example.restaurantordersystem.model.Payment;

import java.util.List;

public interface PaymentsDAO {
    Payment findPaymentByID(int paymentID);

    List<Payment> findAllPayments();

    boolean savePayment(Payment payment);

    boolean deletePayment(int id);
}
