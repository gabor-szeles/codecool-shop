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


/**
 * Db_handler is the class providing connection to postgreSQL database.
 */
public class Db_handler {

    /**
     * Db handler instance to be returned every time.
     */
    private static Db_handler instance = null;

    /**
     * A private Constructor prevents any other class from instantiating.
     */
    private Db_handler() {
    }

    /**
     * Returns the database handler object
     * @return Db_handler object instance.
     */
    public static Db_handler getInstance() {
        if (instance == null) {
            instance = new Db_handler();
        }
        return instance;
    }

    /**
     * Establishes a connection with postgresSQL database.
     * @throws SQLException if connection cannot be established.
     * @return Returns Connection object to the DB.
     */
    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        FileInputStream in;
        try {
            in = new FileInputStream("configuration/DataBase.properties");
            try {
                props.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Executes a PreparedStatement when there is a ResultSet object to return.
     * @param preparedStatement PreparedStatement object to be executed.
     * @return ResultSet object as a cursor in DB.
     */
    private ResultSet executePreparedStatementQuery(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes a PreparedStatement when there is no ResultSet object to return.
     * @param preparedStatement PreparedStatement object to be executed.
     */
    private void executePreparedStatement(PreparedStatement preparedStatement) {
        try {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and executes a PreparedStatement for adding BaseModel object to DB.
     * @param object BaseModel object which needs to be added to DB.
     * @param query Query to be rendered as PreparedStatement.
     */
    public void createPreparedStatementForAdd(BaseModel object, String query) {
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(query);
            switch (object.getClass().getSimpleName()) {
                case "Product":
                    fillPreparedStatementFields((Product) object, prepStatement);
                    break;
                case "ProductCategory":
                    fillPreparedStatementFields((ProductCategory) object, prepStatement);
                    break;
                case "Supplier":
                    fillPreparedStatementFields((Supplier) object, prepStatement);
                    break;
            }
            executePreparedStatement(prepStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills the PreparedStatementFields's for adding ProductCategory to DB.
     * @param productCategory ProductCategory object which needs to be added to DB.
     * @param prepStatement PreparedStatement object to be filled with data from productCategory object.
     * @throws SQLException if the number of fields in PreparedStatement doesn't match the number of arguments to be filled.
     */
    private void fillPreparedStatementFields(ProductCategory productCategory, PreparedStatement prepStatement) throws SQLException {
        prepStatement.setInt(1, productCategory.getId());
        prepStatement.setString(2, productCategory.getName());
        prepStatement.setString(3, productCategory.getDescription());
        prepStatement.setString(4, productCategory.getDepartment());
    }

    /**
     * Fills the PreparedStatement's fields for adding Supplier to DB.
     * @param supplier Supplier object which needs to be added to DB.
     * @param prepStatement PreparedStatement object to be filled with data from supplier object.
     @throws SQLException if the number of fields in PreparedStatement doesn't match the number of arguments to be filled.
     */
    private void fillPreparedStatementFields(Supplier supplier, PreparedStatement prepStatement) throws SQLException {
        prepStatement.setInt(1, supplier.getId());
        prepStatement.setString(2, supplier.getName());
        prepStatement.setString(3, supplier.getDescription());
    }

    /**
     * Fills the PreparedStatement's fields for adding Product to DB.
     * @param product Product object which needs to be added to DB.
     * @param prepStatement PreparedStatement object to be filled with data from Product object.
     @throws SQLException if the number of fields in PreparedStatement doesn't match the number of arguments to be filled.
     */
    private void fillPreparedStatementFields(Product product, PreparedStatement prepStatement) throws SQLException {
        prepStatement.setInt(1, product.getId());
        prepStatement.setString(2, product.getName());
        prepStatement.setString(3, product.getDescription());
        prepStatement.setString(4, product.getDefaultCurrency().getCurrencyCode());
        prepStatement.setFloat(5, product.getDefaultPrice());
        prepStatement.setInt(6, product.getProductCategory().getId());
        prepStatement.setInt(7, product.getSupplier().getId());
    }

    /**
     * Creates and executes a PreparedStatement for finding record in DB.
     * @param id ID of the wanted element.
     * @param query Query to be rendered as PreparedStatement.
     * @return ResultSet object with pointer to found element.
     */
    public ResultSet createPreparedStatementForFind(int id, String query) {
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, id);
            return executePreparedStatementQuery(prepStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates and executes a PreparedStatement for removing record from DB.
     * @param id ID of the wanted element.
     * @param query Query to be rendered as PreparedStatement.
     */
    public void createPreparedStatementForRemove(int id, String query) {
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, id);
            executePreparedStatement(prepStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and executes a PreparedStatement for getting all records in from selected DB table.
     * @param query Query to be rendered as PreparedStatement.
     * @return ResultSet object with pointer to found elements.
     */
    public ResultSet createPreparedStatementForGetAll(String query) {
        try {
            Connection conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
