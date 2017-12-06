package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;
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
//        ProductDaoMem productDataStore = ProductDaoMem.getInstance();
//        List<Product> products = productDataStore.getBy(targetSupplier);

        Utils utils = Utils.getInstance();
        return utils.toJson(data);
    }
}
