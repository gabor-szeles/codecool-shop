package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.ConnectionDetails;
import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductAttributeDao;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductAttributeDao<ProductCategory> {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static ProductCategoryDaoJdbc instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoJdbc.class);

    /* A private Constructor prevents any other class from instantiating.
     */

    /**
     * ProductCategoryDaoJdbc provides access to ProductCategory objects through the SQL database
     */
    private ProductCategoryDaoJdbc() {
    }

    /**
     * Returns the data access object for JDBC
     */
    public static ProductCategoryDaoJdbc getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJdbc();
        }
        return instance;
    }

    public ProductCategory find(int id) {
        String query = "SELECT * FROM product_category WHERE id = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            if(result.next()) {
                return getProductCategory(result);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM product_category";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            List<ProductCategory> productCategories = new ArrayList<>();
            while (result.next()) {
                ProductCategory productCategory = getProductCategory(result);
                productCategories.add(productCategory);
            }
            return productCategories;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("ProductCategory table empty!");
            return null;
        }
    }

    private ProductCategory getProductCategory(CachedRowSet result) throws SQLException {
        return new ProductCategory(
                result.getInt("id"),
                result.getString("name"),
                result.getString("department"),
                result.getString("description"));
    }
}
