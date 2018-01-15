package com.codecool.shop;

import com.codecool.shop.model.BaseModel;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Db_handler {

    private static final Logger logger = LoggerFactory.getLogger(Db_handler.class);
    private static Db_handler instance = null;
    private static Properties props = null;


    private Db_handler() {
    }

    public static Db_handler getInstance() {
        if (instance == null) {
            instance = new Db_handler();
        }
        return instance;
    }


    public Connection getConnection() throws SQLException {
        if (props == null) {
            props = new Properties();
            FileInputStream in = null;
            try {
                in = new FileInputStream("configuration/DataBase.properties");
                logger.debug("Property file source for DB set to: {}.", in);
                try {
                    props.load(in);
                    in.close();
                    logger.info("Successfully loaded the properties for DB.");
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.warn("Unsuccessful load for properties.");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.error("Error in file source for properties.");
            }
        }
        String url = props.getProperty("jdbc.url");
        logger.debug("Database URL set to: {}", url);
        String username = props.getProperty("jdbc.username");
        logger.debug("Database username set to: {}", username);
        String password = props.getProperty("jdbc.password");
        logger.debug("Database password set to: {}", password); // I don't know if this is a good idea //

        return DriverManager.getConnection(url, username, password);
    }

    private ResultSet executePreparedStatementQuery(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void executePreparedStatement(PreparedStatement preparedStatement) {
        try {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPreparedStatementForAdd(BaseModel object, String query) {
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(query);
            logger.info("Creating prepared statement for adding data.");
            switch (object.getClass().getSimpleName()) {
                case "Product":
                    logger.info("Filling prepared statement for Product adding.");
                    fillPreparedStatementFields((Product) object, prepStatement);
                    break;
                case "ProductCategory":
                    logger.info("Filling prepared statement for ProductCategory adding.");
                    fillPreparedStatementFields((ProductCategory) object, prepStatement);
                    break;
                case "Supplier":
                    logger.info("Filling prepared statement for Supplier adding.");
                    fillPreparedStatementFields((Supplier) object, prepStatement);
                    break;
            }
            executePreparedStatement(prepStatement);
            logger.info("Executed SQL statement for adding data.");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error in execution for adding data to DB.");
        }
    }

    private void fillPreparedStatementFields(ProductCategory productCategory, PreparedStatement prepStatement) throws SQLException {
        prepStatement.setInt(1, productCategory.getId());
        prepStatement.setString(2, productCategory.getName());
        prepStatement.setString(3, productCategory.getDescription());
        prepStatement.setString(4, productCategory.getDepartment());
    }

    private void fillPreparedStatementFields(Supplier supplier, PreparedStatement prepStatement) throws SQLException {
        prepStatement.setInt(1, supplier.getId());
        prepStatement.setString(2, supplier.getName());
        prepStatement.setString(3, supplier.getDescription());
    }

    private void fillPreparedStatementFields(Product product, PreparedStatement prepStatement) throws SQLException {
        prepStatement.setInt(1, product.getId());
        prepStatement.setString(2, product.getName());
        prepStatement.setString(3, product.getDescription());
        prepStatement.setString(4, product.getDefaultCurrency().getCurrencyCode());
        prepStatement.setFloat(5, product.getDefaultPrice());
        prepStatement.setInt(6, product.getProductCategory().getId());
        prepStatement.setInt(7, product.getSupplier().getId());
    }

    public ResultSet createPreparedStatementForFind(int id, String query) {
        try {
            Connection conn = getConnection();
            logger.info("Creating prepared statement for finding values in DB.");
            PreparedStatement prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, id);
            logger.info("Executing SQL statement for finding values in DB.");
            return executePreparedStatementQuery(prepStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error in execution for finding statement in DB.");
        }
        return null;
    }

    public void createPreparedStatementForRemove(int id, String query) {
        try {
            Connection conn = getConnection();
            logger.info("Creating prepared statement for removing values in DB.");
            PreparedStatement prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, id);
            executePreparedStatement(prepStatement);
            logger.info("Executed SQL statement for removing data from DB.");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error in execution for removing data from DB.");
        }
    }

    public ResultSet createPreparedStatementForGetAll(String query) {
        try {
            Connection conn = getConnection();
            logger.info("Creating prepared statement for getting all fields of selected table.");
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            logger.info("Executing SQL statement for getting all fields of selected table.");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error in execution for getting all fields of selected table.");
        }
        return null;
    }

}
