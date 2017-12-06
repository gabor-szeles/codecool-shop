package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.*;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class ProductController {

    public static String renderProducts(Request req, Response res) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();

        Map params = new HashMap<>();
        params.put("category", productCategoryDataStore.find(1));
        params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        Utils utils = Utils.getInstance();
        return utils.renderTemplate(params, "product/index");
    }

    public static String handleOrder(Request req, Response res) {
        Map<String,String> request  = Utils.parseJson(req);
        Product targetItem = ProductDaoMem.getInstance().find(Integer.parseInt(request.get("productid")));
        LineItem newLineItem = new LineItem(targetItem, targetItem.getDefaultPrice());
        Order.getCurrentOrder().add(newLineItem);
        Map<String, String> response = new HashMap<>();
        response.put("items-number", Integer.toString(Order.getCurrentOrder().getAddedItems().size()));
        response.put("total-price", Float.toString(Order.getCurrentOrder().getTotalPrice()));
        return Utils.toJson(response);
    }

    private static String renderTemplate(Map model, String template) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, template));
    }
}
