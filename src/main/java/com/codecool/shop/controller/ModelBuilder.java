package com.codecool.shop.controller;

import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder {

    private static ModelBuilder instance = null;

    private ModelBuilder() {}

    public static ModelBuilder getInstance() {
        if (instance == null) {
            instance = new ModelBuilder();
        }
        return instance;
    }

    public static List<Map> productModel(List<Product> products) {
        List<Map> model = new ArrayList<>();
        for (Product product : products) {
            Map<String, String> currentProduct = new HashMap<>();
            currentProduct.put("id", String.valueOf(product.getId()));
            currentProduct.put("name", product.getName());
            currentProduct.put("description", product.getDescription());
            currentProduct.put("price", product.getPrice());

            model.add(currentProduct);
        }

        return model;
    }

    public static List<Map> lineItemModel(List<LineItem> lineItems) {
        List<Map> model = new ArrayList<>();
        for (LineItem lineItem : lineItems) {
            Map<String, String> currentLineItem = new HashMap<>();
            currentLineItem.put("name", lineItem.getItem().getName() );
            currentLineItem.put("quantity", Integer.toString(lineItem.getQuantity()) );
            currentLineItem.put("price", Float.toString(lineItem.getItemPriceSum()) );
            model.add(currentLineItem);
        }

        return model;
    }
}
