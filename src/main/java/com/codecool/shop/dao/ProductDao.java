

package com.codecool.shop.dao;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.List;

/**
 * ProductDao is the interface providing access to Product data in the memory/database.
 */

public interface ProductDao {

    /**
     * Adds a product object to the memory or a record to the database
     * @param product Product object to be added
     */
    void add(Product product);

    /**
     * Finds a product in the memory/database by its id
     * @param id The id of the product to be found
     * @return Returns the Product object found
     */
    Product find(int id);

    /**
     * Removes a product from the memory/database by its id
     * @param id The id of the product to be removed
     */
    void remove(int id);

    /**
     * Returns a List of every Product found in the memory or the database
     * @return A list of Product objects
     */
    List<Product> getAll();

    /**
     * Returns a List of Products that has the same Supplier as provided in the argument
     * @param supplier Supplier object
     * @return Returns a List of Product objects
     */
    List<Product> getBy(Supplier supplier);

    /**
     * Returns a List of Products that has the same category as provided in the argument
     * @param productCategory ProductCategory object
     * @return Returns a List of Product objects
     */
    List<Product> getBy(ProductCategory productCategory);

}
