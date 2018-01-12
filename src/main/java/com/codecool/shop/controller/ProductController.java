package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.BaseDao;
import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.implementation.Db.SupplierDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.model.*;
import spark.Request;
import spark.Response;

import java.util.*;

public class ProductController {

    public static String renderProducts(Request req, Response res) {
        UserController.ensureUserIsLoggedIn(req, res);
        BaseDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDaoJdbc supplierDataStore = SupplierDaoJdbc.getInstance();

        int supplierId = 1;
        Supplier targetSupplier = supplierDataStore.find(supplierId);
        List products = targetSupplier.getProducts();

        List<Map> productsResponse = ModelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("username", req.session().attribute("username"));
        data.put("categories", productCategoryDataStore.getAll());
        data.put("suppliers", supplierDataStore.getAll());
        data.put("collectionName", targetSupplier.getName());
        data.put("collection", productsResponse);

        return Utils.renderTemplate(data, "product/index");
    }

    public static String getProductsBySupplier(Request request, Response response) {
        int supplierId = Integer.parseInt(request.params("id"));
        SupplierDaoJdbc supplierDataStore = SupplierDaoJdbc.getInstance();
        Supplier targetSupplier = supplierDataStore.find(supplierId);
        List<Product> products = targetSupplier.getProducts();

        List<Map> collection = ModelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);
        data.put("collectionName", targetSupplier.getName());

        return Utils.toJson(data);
    }

    public static String getProductsByCategory(Request request, Response response) {
        int categoryId = Integer.parseInt(request.params("id"));
        ProductCategoryDaoJdbc productCategoryDataStore = ProductCategoryDaoJdbc.getInstance();
        ProductCategory targetCategory = productCategoryDataStore.find(categoryId);
        List<Product> products = targetCategory.getProducts();

        List<Map> collection = ModelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);
        data.put("collectionName", targetCategory.getName());

        return Utils.toJson(data);
    }

    public static String handleOrder(Request req, Response res) {
        int productId = Integer.parseInt(req.params("id"));
        Product targetItem = ProductDaoMem.getInstance().find(productId);
        if (!isLineItem(targetItem)) {
            LineItem newLineItem = new LineItem(targetItem, targetItem.getDefaultPrice());
            Order.getCurrentOrder().add(newLineItem);
        }

        Map<String, Object> response = getShoppingCartData();
        return Utils.toJson(response);
    }

    public static String addUserData(Request request, Response response) {
        Map<String, String> userData = Utils.parseJson(request);

        Order.getCurrentOrder().setUserData(userData);

        String res = "order updated with user data";
        return Utils.toJson(res);
    }

    public static String addPaymentData(Request request, Response response) {
        Map<String, String> userData = Utils.parseJson(request);
        Order.getCurrentOrder().setPaymentData(userData);

        String res = "order updated with payment data";
        new Order();
        return Utils.toJson(res);
    }

    private static boolean isLineItem(Product targetItem) {
        for (LineItem lineItem : Order.getCurrentOrder().getAddedItems()) {
            if (lineItem.getItem().equals(targetItem)) {
                lineItem.incrementQuantity();
                return true;
            }
        }
        return false;
    }

    public static String changeQuantity(Request req, Response res) {
        Map<String, String> data = Utils.parseJson(req);
        List<LineItem> lineItems = Order.getCurrentOrder().getAddedItems();
        LineItem targetLineItem = null;
        for (LineItem lineItem : lineItems) {
            if (lineItem.getItem().getId() == Integer.parseInt(data.get("Id"))) {
                targetLineItem = lineItem;
                break;
            }
        }
        if (Objects.equals(data.get("change"), "plus")) {
            if (targetLineItem != null) {
                targetLineItem.incrementQuantity();
            }
            Order.getCurrentOrder().changeTotalPrice();
        } else {
            if (targetLineItem != null && targetLineItem.getQuantity() > 0) {
                targetLineItem.decrementQuantity();
                if (targetLineItem.getQuantity() == 0) {
                    Order.getCurrentOrder().getAddedItems().remove(targetLineItem);
                }

            }
            Order.getCurrentOrder().changeTotalPrice();
        }
        Map<String, Object> response = getShoppingCartData();
        return Utils.toJson(response);
    }

    private static Map<String, Object> getShoppingCartData() {
        List<LineItem> orderItems = Order.getCurrentOrder().getAddedItems();
        List<Map> orders = ModelBuilder.lineItemModel(orderItems);
        Map<String, Object> response = new HashMap<>();
        response.put("itemsNumber", Integer.toString(Order.getCurrentOrder().getTotalSize()));
        response.put("totalPrice", Float.toString(Order.getCurrentOrder().getTotalPrice()));
        response.put("shoppingCart", orders);

        return response;
    }
}
