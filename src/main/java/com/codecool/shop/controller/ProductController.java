package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.*;

import com.sun.org.apache.xpath.internal.operations.Or;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

    public static String renderProducts(Request req, Response res) {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDaoMem supplierDataStore = SupplierDaoMem.getInstance();
        Utils utils = Utils.getInstance();

        int supplierId = 1;
        Supplier targetSupplier = supplierDataStore.find(supplierId);
        List<Product> products = targetSupplier.getProducts();

        List<Map> productsResponse = new ArrayList<>();
        for (Product product : products) {
            Map<String, String> currentProduct = new HashMap<>();
            currentProduct.put("id", String.valueOf(product.getId()));
            currentProduct.put("name", product.getName());
            currentProduct.put("description", product.getDescription());
            currentProduct.put("price", product.getPrice());

            productsResponse.add(currentProduct);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("categories", productCategoryDataStore.getAll());
        data.put("suppliers", supplierDataStore.getAll());
        data.put("collectionName", targetSupplier.getName());
        data.put("collection", productsResponse);

        return utils.renderTemplate(data, "product/index");
    }

    public static String getSupplier(Request request, Response response) {
        int supplierId = Integer.parseInt(request.params("id"));
        SupplierDaoMem supplierDataStore = SupplierDaoMem.getInstance();
        Supplier targetSupplier = supplierDataStore.find(supplierId);
        List<Product> products = targetSupplier.getProducts();

        List<Map> collection = new ArrayList<>();
        for (Product product : products) {
            Map<String, String> currentProduct = new HashMap<>();
            currentProduct.put("id", String.valueOf(product.getId()));
            currentProduct.put("name", product.getName());
            currentProduct.put("description", product.getDescription());
            currentProduct.put("price", product.getPrice());

            collection.add(currentProduct);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);
        data.put("collectionName", targetSupplier.getName());

        Utils utils = Utils.getInstance();
        return utils.toJson(data);
    }

    public static String handleOrder(Request req, Response res) {
        Map<String,String> request  = Utils.parseJson(req);
        Product targetItem = ProductDaoMem.getInstance().find(Integer.parseInt(request.get("productid")));
        if (!isLineItem(targetItem)) {
            LineItem newLineItem = new LineItem(targetItem, targetItem.getDefaultPrice());
            Order.getCurrentOrder().add(newLineItem);
        }
        Map<String, Object> response = new HashMap<>();
        List<Map> orders = new ArrayList<>();
        List<LineItem> orderItems = Order.getCurrentOrder().getAddedItems();
        for (int i = 0; i < orderItems.size(); i++) {
            Map<String, String> productMap = new HashMap<>();
            productMap.put("name", orderItems.get(i).getItem().getName() );
            productMap.put("quantity", Integer.toString(orderItems.get(i).getQuantity()) );
            productMap.put("price", Float.toString(orderItems.get(i).getItemPriceSum()) );
            orders.add(productMap);
        }
        response.put("itemsNumber", Integer.toString(Order.getCurrentOrder().getTotalSize()));
        response.put("totalPrice", Float.toString(Order.getCurrentOrder().getTotalPrice()));
        response.put("shoppingCart", orders);
        System.out.println(response);
        return Utils.toJson(response);
    }

    public static String reviewCart(Request req, Response res) {
        Map<String, List> response = new HashMap<>();
        List<Map> mapList = new ArrayList<>();
        List<LineItem> orderItems = Order.getCurrentOrder().getAddedItems();
        for (int i = 0; i < orderItems.size(); i++) {
            Map<String, String> productMap = new HashMap<>();
            productMap.put("name", orderItems.get(i).getItem().getName() );
            productMap.put("quantity", Integer.toString(orderItems.get(i).getQuantity()) );
            productMap.put("price", Float.toString(orderItems.get(i).getItemPriceSum()) );
            mapList.add(productMap);
        }
        response.put("shoppingCart", mapList);


        return Utils.toJson(response);

    }

    private static boolean isLineItem(Product targetItem) {
        for (LineItem lineItem: Order.getCurrentOrder().getAddedItems()) {
            if (lineItem.getItem().equals(targetItem)){
                lineItem.incrementQuantity();
                Order.getCurrentOrder().incrementTotalSize();
                return true;
            }
        }
        return false;
    }

}
