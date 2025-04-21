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
            response.sendRedirect("home.jsp"); // or wherever you want to go after login
        } else {
            // Login failed
            request.setAttribute("error", "Invalid email or password");
            List<User> users = userDAO.findAllUsers();
            for (User user : users) {
                System.out.println("User: " + user.getEmail() + " | " + user.getPassword());
            }
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
