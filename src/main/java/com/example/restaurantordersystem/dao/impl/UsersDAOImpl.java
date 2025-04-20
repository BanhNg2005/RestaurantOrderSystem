package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.UsersDAO;
import com.example.restaurantordersystem.model.User;
import com.example.restaurantordersystem.db.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAOImpl implements UsersDAO {

    @Override
    public User findUserByID(int userId) {
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String role = rs.getString("role");
                return new User(userId, fullName, password, email, role);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("full_name");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String role = rs.getString("role");
                users.add(new User(id, username, password, email, role));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return users;
    }

    @Override
    public boolean saveUser(User user) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            //Check if user exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE user_id = ?");
            checkStmt.setInt(1, user.getUserId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count == 0) {
                // Insert new user
                stmt = conn.prepareStatement("INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)");
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getRole());
            } else {
                // Update existing user
                stmt = conn.prepareStatement("UPDATE users SET full_name = ?, email = ?, password = ?, role = ? WHERE user_id = ?");
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getRole());
                stmt.setInt(5, user.getUserId());
            }
            int result = stmt.executeUpdate();
            return result > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean deleteUser(int id) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
