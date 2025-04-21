package com.example.restaurantordersystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private List<OrderItem> orderItems;
    private int tableNumber;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private LocalDateTime completionTime;
    private boolean isPaid;
    private double totalAmount;

    public Order() {}
    public Order(int orderId, int tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.orderItems = new ArrayList<>();
        this.status = OrderStatus.PLACED;
        this.orderTime = LocalDateTime.now();
        this.isPaid = false;
        this.totalAmount = 0.0;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public double getTotalAmount() {
        return totalAmount;
    }


    //setters

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public void addItem(OrderItem item) {
        orderItems.add(item);
        calculateTotal(); // recalculate total after adding an item
    }



    public void removeItem(int orderItemId) {
        orderItems.removeIf(item -> item.getId() == orderItemId);
        calculateTotal(); // recalculate total after removal
    }

    private void calculateTotal() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        if (newStatus == OrderStatus.COMPLETED) {
            this.completionTime = LocalDateTime.now();
        }
    }

    public void markAsPaid() {
        this.isPaid = true;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderItems=" + orderItems +
                ", tableNumber=" + tableNumber +
                ", status=" + status +
                ", orderTime=" + orderTime +
                ", completionTime=" + completionTime +
                ", isPaid=" + isPaid +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
