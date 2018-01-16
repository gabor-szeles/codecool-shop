package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.ConnectionDetails;
import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductAttributeDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJdbc implements ProductAttributeDao<Order> {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static OrderDaoJdbc instance = null;
    private static ProductDaoJdbc productDaoInstance = ProductDaoJdbc.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OrderDaoJdbc.class);

    private OrderDaoJdbc () {
    }

    public static OrderDaoJdbc getInstance() {
        if (instance == null) {
            instance = new OrderDaoJdbc();
        }
        return instance;
    }

    public static Integer checkActiveOrder(Integer userId) {
        String query = "SELECT order_id FROM order_detail WHERE id = ? AND is_active = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setBoolean(2, true);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            if(result.next()) {
                return result.getInt("order_id");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Order createOrderFromData(Integer orderId, Integer userId) {
        List<LineItem> lineItems = getLineItemData(orderId);
        return new Order(lineItems, userId);
    }

    private static List<LineItem> createLineItemList(CachedRowSet resultSet) throws SQLException {
        List<LineItem> lineItemList = new ArrayList<>();
        while (resultSet.next()) {
            int quantity = resultSet.getInt("quantity");
            int productId = resultSet.getInt("product_id");
            Product lineItemProduct = productDaoInstance.find(productId);
            lineItemList.add(new LineItem(lineItemProduct, quantity));
        }
        return lineItemList;
    }

    private static List<LineItem> getLineItemData(Integer orderId) {
        String query = "SELECT * FROM order_detail WHERE order_id = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            return createLineItemList(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
