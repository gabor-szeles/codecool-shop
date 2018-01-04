package com.codecool.shop;

import com.codecool.shop.model.BaseModel;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.*;

public class Db_handler {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static Db_handler instance = null;


    private Db_handler() {
    }

    public static Db_handler getInstance() {
        if (instance == null) {
            instance = new Db_handler();
        }
        return instance;
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
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

    public ResultSet createPreparedStatementForFindOrRemove(int id, String query) {
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
