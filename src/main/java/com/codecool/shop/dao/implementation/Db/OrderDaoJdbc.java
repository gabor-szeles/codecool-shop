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
        String query = "SELECT order_id FROM order_detail WHERE user_id = ? AND is_active = ?";
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
            return generateOrder(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Order createOrderFromData(Integer orderId, Integer userId) {
        List<LineItem> lineItems = getLineItemData(orderId);
        int orderTotalSize = lineItems.stream().mapToInt(LineItem::getQuantity).sum();
        return new Order(lineItems, userId, orderTotalSize);
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
        String query = "SELECT * FROM order_detail INNER JOIN product ON order_detail.product_id = product.id WHERE order_id = ? ORDER BY product.name ASC";
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

    public static void addLineItem(LineItem lineItem, int orderId, int userId) {
        String query = "INSERT INTO order_detail (order_id, user_id, is_active, product_id, quantity) VALUES (?, ?, ?, ? ,?)";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            statement.setInt(2, userId);
            statement.setBoolean(3, true);
            statement.setInt(4, lineItem.getItem().getId());
            statement.setInt(5, lineItem.getQuantity());
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            db_handler.modifyQuery(connectionDetails);
            logger.info("Lineitem added to database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateLineItem(LineItem lineItem, int orderId) {
        String query = "UPDATE order_detail SET quantity = ? WHERE order_id = ? AND product_id = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, lineItem.getQuantity());
            statement.setInt(2, orderId);
            statement.setInt(3, lineItem.getItem().getId());
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            db_handler.modifyQuery(connectionDetails);
            logger.debug("Lineitem updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deactivateLineItem(LineItem lineItem, int orderId) {
        String query = "UPDATE order_detail SET is_active = ? WHERE order_id = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(1, false);
            statement.setInt(2, orderId);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            db_handler.modifyQuery(connectionDetails);
            logger.debug("Lineitem deactivade");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeLineItem(LineItem lineItem, int orderId) {
        String query = "DELETE FROM order_detail WHERE order_id = ? AND product_id = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            statement.setInt(2, lineItem.getItem().getId());
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            db_handler.modifyQuery(connectionDetails);
            logger.debug("Lineitem deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Integer generateOrder(int userId) {
        String query = "INSERT INTO orders (user_id, is_active) VALUES (?, ?) RETURNING order_id";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setBoolean(2, true);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            if (result.next()) {
                Integer id = result.getInt("order_id");
                return id;
            }
            logger.debug("Order added to database");
            return null;
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
