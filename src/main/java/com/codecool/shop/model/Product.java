package com.codecool.shop.model;

public class Product extends BaseModel {

    private float defaultPrice;

    public Product(int id, String name, String description, float defaultPrice) {
        super(id, name, description);
        this.defaultPrice = defaultPrice;
    }

    public float getDefaultPrice() {
        return defaultPrice;
    }
}
