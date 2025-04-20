package com.example.restaurantordersystem.dao;

import com.example.restaurantordersystem.model.User;
import java.util.List;

public interface UsersDAO {
    User findUserByID(int userId);

    List<User> findAllUsers();

    boolean saveUser(User user);

    boolean deleteUser(int id);
}
