package com.codecool.shop.dao.implementation.Db;


import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.BaseDao;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements BaseDao<ProductCategory> {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static ProductCategoryDaoJdbc instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoJdbc.class);

    /* A private Constructor prevents any other class from instantiating.
     */

    /**
     * ProductCategoryDaoJdbc provides access to ProductCategory objects through the SQL database
     */
    private ProductCategoryDaoJdbc() {
    }

    /**
     * Returns the data access object for JDBC
     */
    public static ProductCategoryDaoJdbc getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJdbc();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        String query = "INSERT INTO product_category (id, name, description, department) " +
                "VALUES (?,?,?,?);";
        logger.debug("Product category add query created");
        db_handler.createPreparedStatementForAdd(category, query);
    }

    /**
     * @implNote returns null if no record is found in the database
     */
    @Override
    public ProductCategory find(int id) {

        ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();

        if (productCategoryDaoMem.getAll().contains(productCategoryDaoMem.find(id))) {
            logger.debug("Memory contains ProductCategory id {}", id);
            return productCategoryDaoMem.find(id);
        } else {

            String query = "SELECT * FROM product_category WHERE id = ?;";

            ResultSet foundElement = db_handler.createPreparedStatementForFind(id, query);
            try {
                foundElement.next();
                ProductCategory foundCategory = new ProductCategory(foundElement.getString("name"),
                        foundElement.getString("department"),
                        foundElement.getString("description"));
                foundCategory.setId(foundElement.getInt("id"));
                productCategoryDaoMem.add(foundCategory);
                logger.debug("Product category {} added to ProductCategoryDaoMem", foundCategory.getName());
                return foundCategory;
            } catch (SQLException e) {
                logger.warn("No SQL entry found for product category id {}", id);
                return null;
            }
        }
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_category WHERE id = ?;";
        db_handler.createPreparedStatementForRemove(id, query);
    }

    /**
     * @throws SQLException when the product_category table is empty
     */
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
                ProductCategoryDaoMem.getInstance().add(newProductCategory);
                productCategories.add(newProductCategory);
            }
        } catch (SQLException e) {
            logger.warn("ProductCategory table empty!");
            return null;
        }

        logger.debug("{} suppliers found", productCategories.size());
        return productCategories;
    }
}
