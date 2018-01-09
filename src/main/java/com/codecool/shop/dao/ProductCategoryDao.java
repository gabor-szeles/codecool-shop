package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;

import java.util.List;

/**
 * ProductCategoryDao is the interface providing access to ProductCategory data in the memory/database.
 */
public interface ProductCategoryDao {

    /**
     * Adds a ProductCategory object to the memory or a record to the database
     * @param category ProductCategory object to be added
     */
    void add(ProductCategory category);

    /**
     * Finds a product category in the memory/database by its id
     * @param id The id of the product category to be found
     * @return Returns the ProductCategory object found
     */
    ProductCategory find(int id);

    /**
     * Removes a product category from the memory/database by its id
     * @param id The id of the product category to be removed
     */
    void remove(int id);

    List<ProductCategory> getAll();

}
