package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductController {

    public static String renderProducts(Request req, Response res) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        Utils utils = Utils.getInstance();

        Map params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        return utils.renderTemplate(params, "product/index");
    }

}
