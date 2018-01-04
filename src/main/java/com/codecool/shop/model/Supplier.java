package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.Db.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.SupplierDaoMem;

import java.util.ArrayList;
import java.util.List;


public class Supplier extends BaseModel {
    private List<Product> products;

    public Supplier(String name, String description) {
        super(name, description);
        this.products = new ArrayList<>();
    }

    public List getProducts() {
        this.products = ProductDaoJdbc.getInstance().getBy(this);
        return this.products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description
        );
    }
}