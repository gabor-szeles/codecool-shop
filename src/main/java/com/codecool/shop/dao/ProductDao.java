

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
