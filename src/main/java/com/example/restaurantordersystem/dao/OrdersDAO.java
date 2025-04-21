package com.example.restaurantordersystem.dao;

import com.example.restaurantordersystem.model.Order;
import java.util.List;

public interface OrdersDAO {
    Order findById(int orderId);
    List<Order> findAll();
    boolean save(Order order);
    boolean delete(int orderId);
}