package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.Db.ProductDaoJdbc;

import java.util.ArrayList;
import java.util.List;

public class ProductCategory extends BaseModel {

    private String department;

    public ProductCategory(int id, String name, String department, String description) {
        super(id, name, description);
        this.department = department;
    }
}