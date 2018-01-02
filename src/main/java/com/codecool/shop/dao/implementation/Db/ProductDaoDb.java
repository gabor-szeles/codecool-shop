package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class ProductDaoDb implements ProductDao {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static ProductDaoDb instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoDb() {
    }

    public static ProductDaoDb getInstance() {
        if (instance == null) {
            instance = new ProductDaoDb();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        String query = "INSERT INTO product (id, name, description, currency_string, default_price, category_id, supplier_id) " +
                "VALUES (?,?,?,?,?,?,?);";

        db_handler.createPreparedStatementForAdd(product, query);
    }

    @Override
    public Product find(int id) {
        // placeholder //
        return new Product("def", 10f, "USD", "def", new ProductCategory("def","def", "def"), new Supplier("def", "def"));
    }

    @Override
    public void remove(int id) {
    }

    @Override
    public List<Product> getAll() {
        return new ArrayList<>();
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return new ArrayList<>();
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return new ArrayList<>();
    }
}
