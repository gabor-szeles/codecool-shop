package com.codecool.shop.dao;

import com.codecool.shop.model.Product;

import java.util.List;

public interface ProductDao {

    Product find(int id);
    List<Product> getBySupplier(int id);
    List<Product> getByProductCategory(int id);
}
