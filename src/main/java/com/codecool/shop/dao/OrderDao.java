package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

/**
 * OrderDao is the interface providing access to Order data in the memory/database.
 */
public interface OrderDao {

    /**
     * Adds an Order object to the memory or a record to the database
     * @param order Order object to be added
     */
    void add(Order order);

    /**
     * Finds an order in the memory/database by its id
     * @param id The id of the order to be found
     * @return Returns the Order object found
     */
    Order find(int id);

    /**
     * Removes an order from the memory/database by its id
     * @param id The id of the order to be removed
     */
    void remove(int id);

    /**
     * Returns a List of every Order found in the memory or the database
     * @return A list of Order objects
     */
    List<Order> getAll();
}
