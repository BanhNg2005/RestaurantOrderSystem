package com.example.restaurantordersystem.dao;

import com.example.restaurantordersystem.model.OrderItem;
import java.util.List;

public interface OrderItemsDAO {
    OrderItem findById(int id);
    List<OrderItem> findAll(int orderId);
    boolean save(OrderItem orderItem);
    boolean update(OrderItem orderItem);
    boolean delete(int id);
}