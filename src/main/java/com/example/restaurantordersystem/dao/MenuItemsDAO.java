package com.example.restaurantordersystem.dao;

import com.example.restaurantordersystem.model.MenuItem;

import java.sql.ResultSet;
import java.util.List;

public interface MenuItemsDAO {
    MenuItem findMenuItemById(int id);
    List<MenuItem> findAllMenuItems();
    boolean updateMenuItem(MenuItem menuItem);
    boolean deleteMenuItemById(int id);
    MenuItem mapResultSetToMenuItem(ResultSet rs);
}
