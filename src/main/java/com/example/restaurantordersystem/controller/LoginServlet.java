package com.example.restaurantordersystem.controller;

import com.example.restaurantordersystem.dao.UsersDAO;
import com.example.restaurantordersystem.dao.impl.UsersDAOImpl;
import com.example.restaurantordersystem.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UsersDAO userDAO = new UsersDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User tempUser= userDAO.validateUser(email, password);

        if (tempUser != null) {
            // Login successful
            HttpSession session = request.getSession();
            session.setAttribute("user", tempUser);

            // Check user role and redirect accordingly
            if ("Admin".equals(tempUser.getRole())) {
                // Admin user - redirect to admin dashboard
                response.sendRedirect(request.getContextPath() + "/admin/home");
            } else {
                // Regular user - redirect to client home
                response.sendRedirect("client_home.jsp");
            }
        } else {
            // Login failed
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
