package com.example.restaurantordersystem.controller;

import com.example.restaurantordersystem.dao.MenuItemsDAO;
import com.example.restaurantordersystem.dao.impl.MenuItemsDAOImpl;
import com.example.restaurantordersystem.model.MenuItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
    private final MenuItemsDAO menuItemDAO = new MenuItemsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<MenuItem> menuItems = menuItemDAO.findAllMenuItems();

        request.setAttribute("menuItems", menuItems);

        request.getRequestDispatcher("/menu.jsp").forward(request, response);
    }
}
