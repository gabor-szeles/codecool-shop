package com.codecool.shop.dao;


import com.codecool.shop.model.User;

/**
 * UserDao is the interface providing access to User data in the database.
 */
public interface UserDao {

    /**
     * Adds a User record to the database
     * @param user User object to be added
     */
    Integer add(User user);

    /**
     * Finds a user in the database by its user name
     * @param username The user name of the user to be found
     * @return Returns the User object found
     * @throws java.sql.SQLException when the user cannot be found in the database
     */
    User find(String username);
}
