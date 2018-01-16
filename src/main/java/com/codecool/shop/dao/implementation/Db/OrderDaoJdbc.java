package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductAttributeDao;
import com.codecool.shop.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderDaoJdbc implements ProductAttributeDao<Order> {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static OrderDaoJdbc instance = null;
    private static final Logger logger = LoggerFactory.getLogger(OrderDaoJdbc.class);

    private OrderDaoJdbc () {
    }

    public static OrderDaoJdbc getInstance() {
        if (instance == null) {
            instance = new OrderDaoJdbc();
        }
        return instance;
    }

    @Override
    public Order find(int id) {
        return null;
    }

    @Override
    public List<Order> getAll() {
        return null;
    }
}
