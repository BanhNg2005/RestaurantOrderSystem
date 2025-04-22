package com.example.restaurantordersystem.controller;

import com.example.restaurantordersystem.dao.MenuItemsDAO;
import com.example.restaurantordersystem.dao.OrdersDAO;
import com.example.restaurantordersystem.dao.ReservationsDAO;
import com.example.restaurantordersystem.dao.TablesDAO;
import com.example.restaurantordersystem.dao.UsersDAO;
import com.example.restaurantordersystem.dao.impl.MenuItemsDAOImpl;
import com.example.restaurantordersystem.dao.impl.OrdersDAOImpl;
import com.example.restaurantordersystem.dao.impl.ReservationsDAOImpl;
import com.example.restaurantordersystem.dao.impl.TablesDAOImpl;
import com.example.restaurantordersystem.dao.impl.UsersDAOImpl;
import com.example.restaurantordersystem.model.Category;
import com.example.restaurantordersystem.model.MenuItem;
import com.example.restaurantordersystem.model.Order;
import com.example.restaurantordersystem.model.OrderStatus;
import com.example.restaurantordersystem.model.Reservation;
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
import java.util.List;

@WebServlet("/admin/home")
public class AdminHomeServlet extends HttpServlet {

    private final MenuItemsDAO menuItemsDAO = new MenuItemsDAOImpl();
    private final TablesDAO tablesDAO = new TablesDAOImpl();
    private final UsersDAO usersDAO = new UsersDAOImpl();
    private final OrdersDAO ordersDAO = new OrdersDAOImpl();
    private final ReservationsDAO reservationsDAO = new ReservationsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Check if user has admin role
        User user = (User) session.getAttribute("user");
        if (!"Admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Fetch data for admin dashboard
        List<MenuItem> menuItems = menuItemsDAO.findAllMenuItems();
        List<Table> tables = tablesDAO.findAllTables();
        List<User> users = usersDAO.findAllUsers();
        List<Order> orders = ordersDAO.findAll();
        List<Reservation> reservations = reservationsDAO.findAllReservations();

        // Set attributes
        request.setAttribute("menuItems", menuItems);
        request.setAttribute("tables", tables);
        request.setAttribute("users", users);
        request.setAttribute("orders", orders);
        request.setAttribute("reservations", reservations);

        // Forward to JSP
        request.getRequestDispatcher("/admin_home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "addMenuItem":
                addMenuItem(request);
                break;
            case "editMenuItem":
                editMenuItem(request);
                break;
            case "deleteMenuItem":
                deleteMenuItem(request);
                break;
            case "addTable":
                addTable(request);
                break;
            case "editTable":
                editTable(request);
                break;
            case "deleteTable":
                deleteTable(request);
                break;
            case "updateOrder":
                updateOrder(request);
                break;
            case "deleteReservation":
                deleteReservation(request);
                break;
        }

        // Redirect to the admin home page
        response.sendRedirect(request.getContextPath() + "/admin/home");
    }

    private void addMenuItem(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        Category category = Category.valueOf(request.getParameter("category"));
        boolean isAvailable = "on".equals(request.getParameter("isAvailable"));

        MenuItem menuItem = new MenuItem(0, name, description, price, category, isAvailable);
        menuItemsDAO.updateMenuItem(menuItem);
    }

    private void editMenuItem(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        Category category = Category.valueOf(request.getParameter("category"));
        boolean isAvailable = "on".equals(request.getParameter("isAvailable"));

        MenuItem menuItem = new MenuItem(id, name, description, price, category, isAvailable);
        menuItemsDAO.updateMenuItem(menuItem);
    }

    private void deleteMenuItem(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        menuItemsDAO.deleteMenuItemById(id);
    }

    private void addTable(HttpServletRequest request) {
        int tableNumber = Integer.parseInt(request.getParameter("tableNumber"));
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        boolean isAvailable = "on".equals(request.getParameter("isAvailable"));

        Table table = new Table(tableNumber, capacity, isAvailable);
        tablesDAO.saveTable(table);
    }

    private void editTable(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        int tableNumber = Integer.parseInt(request.getParameter("tableNumber"));
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        boolean isAvailable = "on".equals(request.getParameter("isAvailable"));

        Table table = new Table(tableNumber, capacity, isAvailable);
        table.setTableId((long) id);
        tablesDAO.saveTable(table);
    }

    private void deleteTable(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        tablesDAO.deleteTable(id);
    }

    private void updateOrder(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        OrderStatus status = OrderStatus.valueOf(request.getParameter("status"));

        Order order = ordersDAO.findById(id);
        if (order != null) {
            order.setStatus(status);
            if (status == OrderStatus.COMPLETED) {
                order.setCompletionTime(LocalDateTime.now());
            }
            ordersDAO.save(order);
        }
    }

    private void deleteReservation(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        reservationsDAO.deleteReservation(id);
    }
}