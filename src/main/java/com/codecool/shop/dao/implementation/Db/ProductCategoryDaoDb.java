package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoDb implements ProductCategoryDao {

    private Db_handler db_handler = Db_handler.getInstance();
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
        return new ProductCategory("def", "def", "def");
    }

    @Override
    public void remove(int id) {
    }

    @Override
    public List<ProductCategory> getAll() {
        return new ArrayList<>();
    }
}
