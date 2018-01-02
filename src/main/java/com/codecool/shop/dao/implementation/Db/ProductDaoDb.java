package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
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

        String query = "SELECT * FROM product WHERE id = ?;";
        SupplierDaoDb supplierDaoDb = SupplierDaoDb.getInstance();
        ProductCategoryDaoDb productCategoryDaoDb = ProductCategoryDaoDb.getInstance();
        ResultSet foundElement = db_handler.createPreparedStatementForFind(id, query);
        System.out.println(foundElement);
        try {

            foundElement.next();
            Product foundProduct = new Product(
                    foundElement.getString("name"),
                    foundElement.getFloat("default_price"),
                    foundElement.getString("currency_string"),
                    foundElement.getString("description"),
                    productCategoryDaoDb.find(foundElement.getInt("category_id")),
                    supplierDaoDb.find(foundElement.getInt("supplier_id")));

            foundProduct.setId(foundElement.getInt("id"));
            System.out.println(foundProduct);
            return foundProduct;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
