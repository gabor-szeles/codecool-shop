package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.ConnectionDetails;
import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductAttributeDao;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJdbc implements ProductAttributeDao<Supplier> {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static SupplierDaoJdbc instance = null;
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoJdbc.class);

    /* A private Constructor prevents any other class from instantiating.
     */

    /**
     * SupplierDaoJdbc provides access to Supplier objects through the SQL database
     */
    private SupplierDaoJdbc() {
    }

    /**
     * Returns the data access object for JDBC
     */
    public static SupplierDaoJdbc getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJdbc();
        }
        return instance;
    }

    public Supplier find(int id) {
        String query = "SELECT * FROM supplier WHERE id = ?";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            if(result.next()) {
                return new Supplier(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Supplier> getAll() {
        String query = "SELECT * FROM supplier";
        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ConnectionDetails connectionDetails = new ConnectionDetails(connection, statement);
            CachedRowSet result = db_handler.fetchQuery(connectionDetails);
            List<Supplier> suppliers = new ArrayList<>();
            while (result.next()) {
                Supplier supplier = new Supplier(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"));
                suppliers.add(supplier);
            }
            return suppliers;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("ProductCategory table empty!");
            return null;
        }
    }
}
