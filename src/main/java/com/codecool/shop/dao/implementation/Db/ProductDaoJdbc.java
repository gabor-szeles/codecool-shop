package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.ConnectionDetails;
import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDaoJdbc provides access to product objects through the SQL database
 */
public class ProductDaoJdbc implements ProductDao {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static ProductDaoJdbc instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoJdbc.class);


    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoJdbc() {
    }

    /**
     * Returns the data access object for JDBC
     */
    public static ProductDaoJdbc getInstance() {
        if (instance == null) {
            instance = new ProductDaoJdbc();
        }
        return instance;
    }

    public Product find(int id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            if(result.next()) {
                return getProduct(result);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Product> getProducts(String query, int id) {
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            List<Product> products = new ArrayList<>();
            while (result.next()) {
                Product product =  getProduct(result);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> getBySupplier(int id) {
        String query = "SELECT * FROM product WHERE supplier_id = ?";
        return getProducts(query, id);
    }

    public List<Product> getByProductCategory(int id) {
        String query = "SELECT * FROM product WHERE category_id = ?";
        return getProducts(query, id);
    }

    private Product getProduct(CachedRowSet result) throws SQLException {
        return new Product (
                result.getInt("id"),
                result.getString("name"),
                result.getString("description"),
                result.getFloat("default_price"));
    }
}
