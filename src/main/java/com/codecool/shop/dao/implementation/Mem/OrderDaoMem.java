package com.codecool.shop.dao.implementation.Mem;

import com.codecool.shop.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderDaoMem provides access to Order objects in the memory
 */
public class OrderDaoMem {
    private static OrderDaoMem instance = null;
    private List<Order> DATA = new ArrayList<>();

    private OrderDaoMem() {
    }

    /**
     * Returns the data access object for memory
     */
    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    public void add(Order order) {
        order.setId(DATA.size() + 1);
        DATA.add(order);
    }

    public Order find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public void remove(int id) {
        DATA.remove(find(id));
    }

    public List<Order> getAll() {
        return DATA;
    }
}
