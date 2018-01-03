package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
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

        ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();

        if (productCategoryDaoMem.getAll().contains(productCategoryDaoMem.find(id))) {
            return productCategoryDaoMem.find(id);
        } else {

            String query = "SELECT * FROM product_category WHERE id = ?;";

            ResultSet foundElement = db_handler.createPreparedStatementForFindOrRemove(id, query);
            try {
                foundElement.next();
                ProductCategory foundCategory = new ProductCategory(foundElement.getString("name"),
                        foundElement.getString("department"),
                        foundElement.getString("description"));
                foundCategory.setId(foundElement.getInt("id"));
                return foundCategory;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_category WHERE id = ?;";
        db_handler.createPreparedStatementForFindOrRemove(id, query);
    }

    @Override
    public List<ProductCategory> getAll() {

        ProductCategoryDaoMem.getInstance().clear();

        ArrayList<ProductCategory> productCategories = new ArrayList<>();
        String query = "SELECT * FROM product_category";
        ResultSet foundElements = db_handler.createPreparedStatementForGetAll(query);

        try {
            while (foundElements.next()){
                ProductCategory newProductCategory = new ProductCategory(foundElements.getString("name"),
                        foundElements.getString("department"),
                        foundElements.getString("description"));
                newProductCategory.setId(foundElements.getInt("id"));

                productCategories.add(newProductCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(productCategories);
        return productCategories;
    }
}
