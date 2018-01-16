package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.implementation.Db.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.Db.SupplierDaoJdbc;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderController {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private static SupplierDaoJdbc supplierDaoJdbc = SupplierDaoJdbc.getInstance();
    private static ProductCategoryDaoJdbc productCategoryDaoJdbc = ProductCategoryDaoJdbc.getInstance();
    private static ProductDaoJdbc productDaoJdbc = ProductDaoJdbc.getInstance();

    public static Order checkAndCreateOrder (Integer userName) {

        return null;
    }
    /**
     * This method is responsible to create a lineitem object that will represent the actual item object in an
     * orderable format
     * @param req request sent from client side
     * @param res response sent after processing the request
     * @return Returns a map with all the lineitems in the sopping cart completed with the newly item in the cart
     */
    public static String handleOrder(Request req, Response res) {
        int productId = Integer.parseInt(req.params("id"));
        Product product = productDaoJdbc.find(productId);
        LOGGER.info("Line-item selected by id ({}) from order request: {}", productId, product);

        if (!isLineItem(product)) {
            LineItem newLineItem = new LineItem(product, product.getDefaultPrice());
            Order.getCurrentOrder().add(newLineItem);

            System.out.println("lineitem: " + newLineItem);

            LOGGER.info("New lineitem was created: {}", newLineItem);
        }

        Map<String, Object> response = getShoppingCartData();

        LOGGER.debug("Shopping card data with the added lineitem to jasonify: {}", response);

        return Utils.toJson(response);
    }

    /**
     * This method is responsible for adding user data to the order.
     * @param request request sent from client side
     * @param response response sent after processing the request
     * @return a confirmation that the update for the order has been processed
     */
    public static String addUserData(Request request, Response response) {
        Map<String, String> userData = Utils.parseJson(request);

        Order.getCurrentOrder().setUserData(userData);

        LOGGER.debug("Userdata to jasonify after reading the request data in: {}", userData);
        LOGGER.debug("order updated with user data");

        return Utils.toJson("OK");
    }

    /**
     * This method is responsible for adding payment data to the order.
     * @param request request sent from client side
     * @param response response sent after processing the request
     * @return a confirmation that the update for the order has been processed
     */
    public static String addPaymentData(Request request, Response response) {
        Map<String, String> userData = Utils.parseJson(request);

        System.out.println("userdata:" + userData);
        Order.getCurrentOrder().setPaymentData(userData);

        LOGGER.debug("Payment data to jasonify after reading the request data in: {}", userData);

        LOGGER.info("Order updated with payment data");
        LOGGER.debug("order updated with payment data");
        new Order();
        return Utils.toJson("OK");
    }

    /**
     * This method is responsible to check if the argument item is a lineitem ready to be processed as a
     * shopping cart item.
     * @param targetItem
     * @return gives the state of the argument item
     */
    private static boolean isLineItem(Product targetItem) {

        LOGGER.debug("Line-item to check if it is lineitem: {}", targetItem);
        System.out.println("WATCH THIS");
        System.out.println(Order.getCurrentOrder().getAddedItems());
        for (LineItem lineItem : Order.getCurrentOrder().getAddedItems()) {
            if (lineItem.getItem().getId() == targetItem.getId()) {
                lineItem.incrementQuantity();

                LOGGER.info("Lineitem is checked, and it returned true");

                return true;
            }
        }

        LOGGER.info("Lineitem is checked, and it returned false");

        return false;
    }

    /**
     * This method is responsible to update the desired quantity of a lineitem of interest upon request
     * @param req request sent from client side
     * @param res response sent after processing the request
     * @return updated sopping cart data in a jasonified format
     */
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

    /**
     * This method gethers all possible data about the shopping cart.
     * @return a map with all the fields of the shopping cart for further processing
     */
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
