package com.example.restaurantordersystem.controller;

import com.example.restaurantordersystem.dao.MenuItemsDAO;
import com.example.restaurantordersystem.dao.OrderItemsDAO;
import com.example.restaurantordersystem.dao.OrdersDAO;
import com.example.restaurantordersystem.dao.TablesDAO;
import com.example.restaurantordersystem.dao.impl.MenuItemsDAOImpl;
import com.example.restaurantordersystem.dao.impl.OrderItemsDAOImpl;
import com.example.restaurantordersystem.dao.impl.OrdersDAOImpl;
import com.example.restaurantordersystem.dao.impl.TablesDAOImpl;
import com.example.restaurantordersystem.model.MenuItem;
import com.example.restaurantordersystem.model.Order;
import com.example.restaurantordersystem.model.OrderItem;
import com.example.restaurantordersystem.model.OrderStatus;
import com.example.restaurantordersystem.model.Table;
import com.example.restaurantordersystem.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private final MenuItemsDAO menuItemsDAO = new MenuItemsDAOImpl();
    private final TablesDAO tablesDAO = new TablesDAOImpl();
    private final OrdersDAO ordersDAO = new OrdersDAOImpl();
    private final OrderItemsDAO orderItemsDAO = new OrderItemsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get menu items from database
        List<MenuItem> menuItems = menuItemsDAO.findAllMenuItems();
        request.setAttribute("menuItems", menuItems);

        // Get available tables
        List<Table> allTables = tablesDAO.findAllTables();
        List<Table> availableTables = allTables.stream()
                .filter(Table::isAvailable)
                .collect(Collectors.toList());

        // If no tables are available, add a message
        if (availableTables.isEmpty()) {
            request.setAttribute("message", "No tables are currently available.");
            // Add at least one table for the UI to work properly
            availableTables.add(new Table(0, 0, false));
        }

        request.setAttribute("availableTables", availableTables);

        // Forward to order.jsp
        request.getRequestDispatcher("/order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            // Get the table number and total amount
            int tableNumber = Integer.parseInt(request.getParameter("tableNumber"));
            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));

            // Create a new order
            Order order = new Order(0, tableNumber);
            order.setOrderTime(LocalDateTime.now());
            order.setStatus(OrderStatus.PLACED);
            order.setTotalAmount(totalAmount);
            order.setPaid(false);

            // Save the order to get an orderId
            if (ordersDAO.save(order)) {
                // Get all order items
                List<Order> orders = ordersDAO.findAll();
                // Find the latest order (assumed to be the one we just created)
                Order latestOrder = orders.stream()
                        .max((o1, o2) -> o1.getOrderId() - o2.getOrderId())
                        .orElse(null);

                if (latestOrder != null) {
                    int orderId = latestOrder.getOrderId();

                    // Get all parameters
                    Enumeration<String> paramNames = request.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = paramNames.nextElement();

                        // Look for parameters named "quantity-X"
                        if (paramName.startsWith("quantity-")) {
                            String itemIdStr = paramName.substring("quantity-".length());
                            int itemId = Integer.parseInt(itemIdStr);
                            int quantity = Integer.parseInt(request.getParameter(paramName));

                            if (quantity > 0) {
                                MenuItem menuItem = menuItemsDAO.findMenuItemById(itemId);
                                if (menuItem != null) {
                                    // Create order item
                                    OrderItem orderItem = new OrderItem(0, orderId, menuItem, quantity);
                                    orderItemsDAO.save(orderItem);
                                }
                            }
                        }
                    }

                    // Set success message
                    request.setAttribute("successMessage", "Your order has been placed successfully! Order #" + orderId);
                } else {
                    request.setAttribute("errorMessage", "Failed to process order. Please try again.");
                }
            } else {
                request.setAttribute("errorMessage", "Failed to save the order. Please try again.");
            }

            // Redirect back to the order page with fresh data
            doGet(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input data. Please try again.");
            doGet(request, response);
        }
    }
}