package com.example.restaurantordersystem.model;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Order order;
    private double amountPaid;
    private LocalDateTime paymentTime;

    public Payment(int id, Order order, PaymentMethod paymentMethod) {
        this.id = id;
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.amountPaid = 0.0;
    }

    public boolean processPayment() {
        // get total from order
        double orderTotal = order.getTotalAmount();

        try {
            switch (paymentMethod) {
                case CASH:
                    try {
                        this.amountPaid = orderTotal;
                        this.paymentStatus = PaymentStatus.COMPLETED;
                        this.paymentTime = LocalDateTime.now();
                        order.markAsPaid();
                        return true;
                    } catch (Exception e) {
                        this.paymentStatus = PaymentStatus.FAILED;
                        return false;
                    }

                case CREDIT_CARD:
                    try {
                        if (orderTotal > 0) {
                            this.amountPaid = orderTotal;
                            this.paymentStatus = PaymentStatus.COMPLETED;
                            this.paymentTime = LocalDateTime.now();
                            order.markAsPaid();
                            return true;
                        } else {
                            throw new Exception("Invalid order total for credit card payment");
                        }
                    } catch (Exception e) {
                        this.paymentStatus = PaymentStatus.FAILED;
                        return false;
                    }

                case DEBIT_CARD:
                    try {
                        if (orderTotal > 0) {
                            this.amountPaid = orderTotal;
                            this.paymentStatus = PaymentStatus.COMPLETED;
                            this.paymentTime = LocalDateTime.now();
                            order.markAsPaid();
                            return true;
                        } else {
                            throw new Exception("Invalid order total for debit card payment");
                        }
                    } catch (Exception e) {
                        this.paymentStatus = PaymentStatus.FAILED;
                        return false;
                    }

                case GIFT_CARD:
                    try {
                        if (orderTotal <= 100) { // assume gift card has $100 limit
                            this.amountPaid = orderTotal;
                            this.paymentStatus = PaymentStatus.COMPLETED;
                            this.paymentTime = LocalDateTime.now();
                            order.markAsPaid();
                            return true;
                        } else {
                            throw new Exception("Order total exceeds gift card limit");
                        }
                    } catch (Exception e) {
                        this.paymentStatus = PaymentStatus.FAILED;
                        return false;
                    }

                case OTHER:
                default:
                    try {
                        // generic payment processing
                        this.amountPaid = orderTotal;
                        this.paymentStatus = PaymentStatus.COMPLETED;
                        this.paymentTime = LocalDateTime.now();
                        order.markAsPaid();
                        return true;
                    } catch (Exception e) {
                        this.paymentStatus = PaymentStatus.FAILED;
                        return false;
                    }
            }
        } catch (Exception e) {
            // catch any unexpected exceptions
            this.paymentStatus = PaymentStatus.FAILED;
            return false;
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Order getOrder() {
        return order;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }


    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }


    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", status=" + paymentStatus +
                ", method=" + paymentMethod +
                ", order=" + order.getOrderId() +
                ", amount=" + amountPaid +
                ", time=" + paymentTime +
                '}';
    }
}
