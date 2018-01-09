package com.codecool.shop.dao.implementation.Mem;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductCategoryDaoMem provides access to ProductCategory objects in the memory
 */
public class ProductCategoryDaoMem implements ProductCategoryDao {

    private static ProductCategoryDaoMem instance = null;
    private List<ProductCategory> DATA = new ArrayList<>();

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoMem() {
    }

    /**
     * Returns the data access object for memory
     */
    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        category.setId(DATA.size() + 1);
        DATA.add(category);
    }

    @Override
    public ProductCategory find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }

    @Override
    public List<ProductCategory> getAll() {
        return DATA;
    }

    /**
     * Clears the list of Supplier objects
     */
    public void clear() {
        DATA.clear();
    }
}
