package com.codecool.shop;

import com.codecool.shop.model.BaseModel;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Cache;


/**
 * Db_handler is the class providing connection to postgreSQL database.
 */
public class Db_handler {


    /**
     * Db handler instance to be returned every time.
     */
    private static final Logger logger = LoggerFactory.getLogger(Db_handler.class);
    private static Db_handler instance = null;
    private static Properties props = null;

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
    public Connection getConnection() {
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
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("Connection failed to database");
            return null;
        }
    }

    public CachedRowSet fetchQuery(ConnectionDetails connectionDetails) {
        Connection connection = connectionDetails.getConnection();
        PreparedStatement statement = connectionDetails.getStatement();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
            CachedRowSet cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);
            return cachedRowSet;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                // I <3 Java
            }
        }
        return null;
    }

    public void modifyQuery(ConnectionDetails connectionDetails) {
        Connection connection = connectionDetails.getConnection();
        PreparedStatement statement = connectionDetails.getStatement();
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
