package com.codecool.shop.dao;


import com.codecool.shop.model.User;

public interface UserDao {
    boolean add(User user);

    User find(String username);
}
