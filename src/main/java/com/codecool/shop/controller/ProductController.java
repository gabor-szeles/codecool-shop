package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.implementation.Db.SupplierDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.*;


public class ProductController {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    public static String renderProducts(Request req, Response res) {
        UserController.ensureUserIsLoggedIn(req, res);
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDaoJdbc supplierDataStore = SupplierDaoJdbc.getInstance();

        LOGGER.info("Data connection established with Memory ({}) and JDBC ({})", productCategoryDataStore, supplierDataStore);

        int supplierId = 1;
        Supplier targetSupplier = supplierDataStore.find(supplierId);

        LOGGER.debug("Supplier created with the id {} : {}", supplierId, targetSupplier);

        List products = targetSupplier.getProducts();

        LOGGER.debug("Target products found on target supplier: {}", products);

        List<Map> productsResponse = ModelBuilder.productModel(products);

        LOGGER.debug("Response with data on target supplier products sent back to server: {}", productsResponse);

        Map<String, Object> data = new HashMap<>();
        data.put("username", req.session().attribute("username"));
        data.put("categories", productCategoryDataStore.getAll());
        data.put("suppliers", supplierDataStore.getAll());
        data.put("collectionName", targetSupplier.getName());
        data.put("collection", productsResponse);

        LOGGER.info("Program finished processing request on loading target supplier data and ready to send back to client" +
                    "with the following data: {}", data);

        return Utils.renderTemplate(data, "product/index");
    }

    public static String getProductsBySupplier(Request request, Response response) {
        int supplierId = Integer.parseInt(request.params("id"));

        LOGGER.info("Supplier id received from request: {}", supplierId);

        SupplierDaoJdbc supplierDataStore = SupplierDaoJdbc.getInstance();
        Supplier targetSupplier = supplierDataStore.find(supplierId);
        List<Product> products = targetSupplier.getProducts();
        List<Map> collection = ModelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);
        data.put("collectionName", targetSupplier.getName());

        LOGGER.debug("Response with supplier data to jasonify: {}", data);

        return Utils.toJson(data);
    }

    public static String getProductsByCategory(Request request, Response response) {
        int categoryId = Integer.parseInt(request.params("id"));

        LOGGER.info("Category id received from request: {}", categoryId);

        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJdbc.getInstance();
        ProductCategory targetCategory = productCategoryDataStore.find(categoryId);
        List<Product> products = targetCategory.getProducts();

        List<Map> collection = ModelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);
        data.put("collectionName", targetCategory.getName());

        LOGGER.debug("Response with category data to jasonify: {}", data);

        return Utils.toJson(data);
    }

    public static String handleOrder(Request req, Response res) {
        int productId = Integer.parseInt(req.params("id"));
        Product targetItem = ProductDaoMem.getInstance().find(productId);

        LOGGER.info("Line-item selected by id ({}) from order request: {}", productId, targetItem);

        if (!isLineItem(targetItem)) {
            LineItem newLineItem = new LineItem(targetItem, targetItem.getDefaultPrice());
            Order.getCurrentOrder().add(newLineItem);

            LOGGER.info("New lineitem was created: {}", newLineItem);
        }

        Map<String, Object> response = getShoppingCartData();

        LOGGER.debug("Shopping card data with the added lineitem to jasonify: {}", response);

        return Utils.toJson(response);
    }

    public static String addUserData(Request request, Response response) {
        Map<String, String> userData = Utils.parseJson(request);

        Order.getCurrentOrder().setUserData(userData);

        LOGGER.debug("Userdata to jasonify after reading the request data in: {}", userData);
        LOGGER.debug("order updated with user data");
        return "";
    }

    public static String addPaymentData(Request request, Response response) {
        Map<String, String> userData = Utils.parseJson(request);
        Order.getCurrentOrder().setPaymentData(userData);

        LOGGER.debug("Payment data to jasonify after reading the request data in: {}", userData);

        LOGGER.info("Order updated with payment data");
        LOGGER.debug("order updated with payment data");
        new Order();
        return "";
    }

    private static boolean isLineItem(Product targetItem) {

        LOGGER.debug("Line-item to check if it is lineitem: {}", targetItem);

        for (LineItem lineItem : Order.getCurrentOrder().getAddedItems()) {
            if (lineItem.getItem().equals(targetItem)) {
                lineItem.incrementQuantity();

                LOGGER.info("Lineitem is checked, and it returned true");

                return true;
            }
        }

        LOGGER.info("Lineitem is checked, and it returned false");

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

                LOGGER.info("Quantity of targetlineitem ({}) has been increased", targetLineItem);

                targetLineItem.incrementQuantity();
            }
            Order.getCurrentOrder().changeTotalPrice();

            LOGGER.info("Total price changed");

        } else {
            if (targetLineItem != null && targetLineItem.getQuantity() > 0) {

                LOGGER.info("Quantity of targetlineitem ({}) has been decreased", targetLineItem);

                targetLineItem.decrementQuantity();
                if (targetLineItem.getQuantity() == 0) {
                    Order.getCurrentOrder().getAddedItems().remove(targetLineItem);

                    LOGGER.info("Targetlineitem ({}) has been removed from order list", targetLineItem);

                }

            }
            Order.getCurrentOrder().changeTotalPrice();

            LOGGER.info("Total price changed");

        }
        Map<String, Object> response = getShoppingCartData();
        return Utils.toJson(response);
    }

    private static Map<String, Object> getShoppingCartData() {
        List<LineItem> orderItems = Order.getCurrentOrder().getAddedItems();

        LOGGER.info("Shopping card data successfully gethered: {}", orderItems);

        List<Map> orders = ModelBuilder.lineItemModel(orderItems);
        Map<String, Object> response = new HashMap<>();
        response.put("itemsNumber", Integer.toString(Order.getCurrentOrder().getTotalSize()));
        response.put("totalPrice", Float.toString(Order.getCurrentOrder().getTotalPrice()));
        response.put("shoppingCart", orders);

        LOGGER.debug("Response with shopping cart data: {}", response);

        return response;
    }
}
