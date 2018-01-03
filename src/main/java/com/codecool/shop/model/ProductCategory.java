package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoDb;
import com.codecool.shop.dao.implementation.Db.ProductDaoDb;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;

import java.util.ArrayList;

public class ProductCategory extends BaseModel {
    private String department;
    private ArrayList<Product> products;  // query to fill this //

    public ProductCategory(String name, String department, String description) {
        super(name, description);
        ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
        this.department = department;
        this.products = new ArrayList<>();
        productCategoryDaoMem.add(this);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList getProducts() {
        return this.products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public String toString() {
        return String.format(
                "id: %1$d," +
                        "name: %2$s, " +
                        "department: %3$s, " +
                        "description: %4$s",
                this.id,
                this.name,
                this.department,
                this.description);
    }
}