package com.example.restaurantordersystem.controller;

import com.example.restaurantordersystem.dao.MenuItemsDAO;
import com.example.restaurantordersystem.dao.OrderItemsDAO;
import com.example.restaurantordersystem.dao.OrdersDAO;
import com.example.restaurantordersystem.dao.PaymentsDAO;
import com.example.restaurantordersystem.dao.impl.MenuItemsDAOImpl;
import com.example.restaurantordersystem.dao.impl.OrderItemsDAOImpl;
import com.example.restaurantordersystem.dao.impl.OrdersDAOImpl;
import com.example.restaurantordersystem.dao.impl.PaymentsDAOImpl;
import com.example.restaurantordersystem.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private final OrdersDAO ordersDAO = new OrdersDAOImpl();
    private final OrderItemsDAO orderItemsDAO = new OrderItemsDAOImpl();
    private final MenuItemsDAO menuItemsDAO = new MenuItemsDAOImpl();
    private final PaymentsDAO paymentsDAO = new PaymentsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("PaymentServlet doGet: User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Debug session attributes
        System.out.println("\n--- PaymentServlet doGet: Session attributes ---");
        System.out.println("tableNumber: " + session.getAttribute("tableNumber"));
        System.out.println("orderTotal: " + session.getAttribute("orderTotal"));
        System.out.println("orderDetails: " + (session.getAttribute("orderDetails") != null ?
                "present (length: " + session.getAttribute("orderDetails").toString().length() + ")" :
                "null"));
        System.out.println("------------------------------------------------\n");

        // Forward to payment page
        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("PaymentServlet: User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Debug all request parameters
        System.out.println("\n--- PaymentServlet doPost: All request parameters ---");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            System.out.println(paramName + ": " + (paramName.equals("orderDetails") ?
                    "[data length: " + paramValue.length() + "]" : paramValue));
        }
        System.out.println("------------------------------------------------\n");

        // First check if this is the initial submission from order.jsp
        String orderDetails = request.getParameter("orderDetails");
        String tableNumber = request.getParameter("tableNumber");
        String orderTotal = request.getParameter("orderTotal");

        System.out.println("PaymentServlet doPost extracted parameters:");
        System.out.println("tableNumber: " + tableNumber);
        System.out.println("orderTotal: " + orderTotal);
        System.out.println("orderDetails length: " + (orderDetails != null ? orderDetails.length() : "null"));

        if (orderDetails != null && tableNumber != null && orderTotal != null
                && !orderDetails.isEmpty() && !tableNumber.isEmpty() && !orderTotal.isEmpty()) {
            // Store the order info in session for the payment page
            session.setAttribute("orderDetails", orderDetails);
            session.setAttribute("tableNumber", tableNumber);
            session.setAttribute("orderTotal", orderTotal);

            System.out.println("PaymentServlet: Stored in session and redirecting to payment page");
            System.out.println("Session values after setting:");
            System.out.println("tableNumber: " + session.getAttribute("tableNumber"));
            System.out.println("orderTotal: " + session.getAttribute("orderTotal"));
            System.out.println("orderDetails length: " +
                    (session.getAttribute("orderDetails") != null ?
                            ((String)session.getAttribute("orderDetails")).length() : "null"));

            // Redirect to payment page to show the form
            response.sendRedirect(request.getContextPath() + "/payment");
            return;
        }

        // Process the payment form submission (card details)
        String cardNumber = request.getParameter("cardNumber");
        String cardName = request.getParameter("cardName");
        String expiry = request.getParameter("expiry");
        String cvv = request.getParameter("cvv");

        System.out.println("Processing payment with card: " +
                (cardNumber != null ? cardNumber.substring(0, 4) + "..." : "null"));

        // Remove spaces from card number for validation
        if (cardNumber != null) {
            cardNumber = cardNumber.replaceAll("\\s+", "");
        }

        // Simple validation (in real app, integrate with payment gateway)
        if (cardNumber != null && cardNumber.matches("\\d{16}")
                && cardName != null && !cardName.isEmpty()
                && expiry != null && expiry.matches("\\d{2}/\\d{2}")
                && cvv != null && cvv.matches("\\d{3,4}")) {

            // Get order info from session
            String savedTableNumber = (String) session.getAttribute("tableNumber");
            String savedOrderTotal = (String) session.getAttribute("orderTotal");
            String savedOrderDetails = (String) session.getAttribute("orderDetails");

            System.out.println("Processing payment with session values:");
            System.out.println("tableNumber: " + savedTableNumber);
            System.out.println("orderTotal: " + savedOrderTotal);
            System.out.println("orderDetails: " + (savedOrderDetails != null ?
                    "length: " + savedOrderDetails.length() : "null"));

            if (savedTableNumber != null && savedOrderTotal != null && savedOrderDetails != null) {
                try {
                    // Create and save the order to the database
                    int tableNum = Integer.parseInt(savedTableNumber);
                    double total = Double.parseDouble(savedOrderTotal);

                    // Create new order
                    Order order = new Order(0, tableNum);
                    order.setOrderTime(LocalDateTime.now());
                    order.setStatus(OrderStatus.PLACED);
                    order.setTotalAmount(total);
                    order.setPaid(true); // Mark as paid immediately since payment is successful

                    // Save order to database to get orderId
                    boolean orderSaved = ordersDAO.save(order);

                    if (orderSaved) {
                        // Get the generated order ID (we need to find the latest order we created)
                        System.out.println("Order saved, fetching newly created order ID");

                        // Find all orders and sort by ID to get the latest one
                        Order latestOrder = ordersDAO.findAll().stream()
                                .max((o1, o2) -> Integer.compare(o1.getOrderId(), o2.getOrderId()))
                                .orElse(null);

                        if (latestOrder != null) {
                            int orderId = latestOrder.getOrderId();
                            System.out.println("Latest order ID: " + orderId);

                            // Make sure to update the order object with the correct ID
                            order.setOrderId(orderId);

                            // Process the order items from JSON
                            try {
                                JSONArray items = new JSONArray(savedOrderDetails);
                                System.out.println("Parsed order items: " + items.length());

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject item = items.getJSONObject(i);
                                    int menuItemId = item.getInt("id");
                                    int quantity = item.getInt("quantity");

                                    // Get the menu item
                                    MenuItem menuItem = menuItemsDAO.findMenuItemById(menuItemId);
                                    if (menuItem != null) {
                                        // Create the order item
                                        OrderItem orderItem = new OrderItem(0, orderId, menuItem, quantity);
                                        orderItemsDAO.save(orderItem);
                                        System.out.println("Saved order item: " + menuItem.getName() +
                                                " x" + quantity);
                                    }
                                }

                                try {
                                    // Create a payment record with the updated order object that has correct ID
                                    Payment payment = new Payment(0, order, PaymentMethod.CREDIT_CARD);
                                    payment.setAmountPaid(total);
                                    payment.setPaymentStatus(PaymentStatus.COMPLETED);
                                    payment.setPaymentTime(LocalDateTime.now());

                                    boolean paymentSaved = paymentsDAO.savePayment(payment);
                                    if (paymentSaved) {
                                        System.out.println("Payment record created in the database");
                                    } else {
                                        System.out.println("Failed to create payment record");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error saving payment: " + e.getMessage());
                                    e.printStackTrace();
                                }

                                // Update the order status
                                order.setStatus(OrderStatus.COMPLETED);
                                order.setPaid(true);
                                order.setCompletionTime(LocalDateTime.now());
                                ordersDAO.update(order);

                                // Success message for the user
                                request.setAttribute("paymentSuccess",
                                        "Payment successful! Thank you for your order #" + orderId +
                                                " at Table " + savedTableNumber + " for $" + savedOrderTotal);

                            } catch (Exception e) {
                                System.out.println("Error processing order items: " + e.getMessage());
                                e.printStackTrace();
                                request.setAttribute("paymentError",
                                        "Error processing your order. Please try again.");
                            }
                        } else {
                            System.out.println("Failed to retrieve latest order ID");
                            request.setAttribute("paymentError",
                                    "Error processing your order. Please try again.");
                        }
                    } else {
                        System.out.println("Failed to save order to database");
                        request.setAttribute("paymentError",
                                "Error saving your order. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing numbers: " + e.getMessage());
                    request.setAttribute("paymentError",
                            "Invalid order data. Please try again.");
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                    e.printStackTrace();
                    request.setAttribute("paymentError",
                            "An unexpected error occurred. Please try again.");
                } finally {
                    // Clear session data regardless of success/failure
                    session.removeAttribute("orderDetails");
                    session.removeAttribute("tableNumber");
                    session.removeAttribute("orderTotal");
                    System.out.println("Session data cleared");
                }
            } else {
                request.setAttribute("paymentError", "Order information missing. Please try again.");
                System.out.println("Payment failed: order info missing from session");
            }
        } else {
            request.setAttribute("paymentError", "Invalid payment details. Please try again.");
            System.out.println("Payment failed: invalid payment details");
        }

        // Forward to payment.jsp to show result
        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }
}
