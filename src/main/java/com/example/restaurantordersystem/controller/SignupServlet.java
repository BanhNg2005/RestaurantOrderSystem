package com.example.restaurantordersystem.controller;

import com.example.restaurantordersystem.dao.UsersDAO;
import com.example.restaurantordersystem.dao.impl.UsersDAOImpl;
import com.example.restaurantordersystem.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private UsersDAO userDAO = new UsersDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = "customer";

        // Optional: Check if user already exists
        if (userDAO.findAllUsers().stream().anyMatch(u -> u.getEmail().equals(email))) {
            request.setAttribute("error", "Email already registered!");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }

        User newUser = new User(0, fullName, password, email, role);
        boolean success = userDAO.saveUser(newUser);

        if (success) {
            response.sendRedirect("login"); // After sign up, redirect to login
        } else {
            request.setAttribute("error", "Sign up failed. Try again.");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
        }
    }
}
