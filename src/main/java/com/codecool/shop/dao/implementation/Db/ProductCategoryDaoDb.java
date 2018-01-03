package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoDb implements ProductCategoryDao {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static ProductCategoryDaoDb instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoDb() {
    }

    public static ProductCategoryDaoDb getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoDb();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        String query = "INSERT INTO product_category (id, name, description, department) " +
                "VALUES (?,?,?,?);";

        db_handler.createPreparedStatementForAdd(category, query);
    }

    @Override
    public ProductCategory find(int id) {
        String query = "SELECT * FROM product_category WHERE id = ?;";

        ResultSet foundElement = db_handler.createPreparedStatementForFindOrRemove(id, query);
        System.out.println(foundElement);
        try {
            foundElement.next();
            ProductCategory foundCategory = new ProductCategory(foundElement.getString("name"),
                    foundElement.getString("department"),
                    foundElement.getString("description"));
            foundCategory.setId(foundElement.getInt("id"));
            System.out.println(foundCategory);
            return foundCategory;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {
    }

    @Override
    public List<ProductCategory> getAll() {
        return new ArrayList<>();
    }
}
