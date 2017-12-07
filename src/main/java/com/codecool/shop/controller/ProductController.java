package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.*;

import spark.Request;
import spark.Response;

import java.util.*;

public class ProductController {

    public static String renderProducts(Request req, Response res) {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDaoMem supplierDataStore = SupplierDaoMem.getInstance();
        Utils utils = Utils.getInstance();

        int supplierId = 1;
        Supplier targetSupplier = supplierDataStore.find(supplierId);
        List<Product> products = targetSupplier.getProducts();

        ModelBuilder modelBuilder = ModelBuilder.getInstance();
        List<Map> productsResponse = modelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("categories", productCategoryDataStore.getAll());
        data.put("suppliers", supplierDataStore.getAll());
        data.put("collectionName", targetSupplier.getName());
        data.put("collection", productsResponse);

        return utils.renderTemplate(data, "product/index");
    }

    public static String getProductsBySupplier(Request request, Response response) {
        int supplierId = Integer.parseInt(request.params("id"));
        SupplierDaoMem supplierDataStore = SupplierDaoMem.getInstance();
        Supplier targetSupplier = supplierDataStore.find(supplierId);
        List<Product> products = targetSupplier.getProducts();

        ModelBuilder modelBuilder = ModelBuilder.getInstance();
        List<Map> collection = modelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);
        data.put("collectionName", targetSupplier.getName());

        Utils utils = Utils.getInstance();
        return utils.toJson(data);
    }

    public static String getProductsByCategory(Request request, Response response) {
        int categoryId = Integer.parseInt(request.params("id"));
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        ProductCategory targetCategory = productCategoryDataStore.find(categoryId);
        List<Product> products = targetCategory.getProducts();

        ModelBuilder modelBuilder = ModelBuilder.getInstance();
        List<Map> collection = modelBuilder.productModel(products);

        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);
        data.put("collectionName", targetCategory.getName());

        Utils utils = Utils.getInstance();
        return utils.toJson(data);
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

    private static boolean isLineItem(Product targetItem) {
        for (LineItem lineItem: Order.getCurrentOrder().getAddedItems()) {
            if (lineItem.getItem().equals(targetItem)){
                lineItem.incrementQuantity();
                return true;
            }
        }
        return false;
    }

    public static String changeQuantity(Request req, Response res) {
        System.out.println("ide bement");
        Map<String, String> data = Utils.parseJson(req);
        List<LineItem> lineItems = Order.getCurrentOrder().getAddedItems();
        LineItem targetLineItem = null;
        for (LineItem lineItem : lineItems ) {
            if (lineItem.getItem().getId() == Integer.parseInt(data.get("Id"))) {
                targetLineItem = lineItem;
                break;
            }
        }
        if (Objects.equals(data.get("change"), "plus")){
            targetLineItem.incrementQuantity();
            Order.getCurrentOrder().changeTotalPrice();
        }
        else {
            if (targetLineItem.getQuantity() > 0) {
                targetLineItem.decrementQuantity();
                if (targetLineItem.getQuantity() == 0) {
                    Order.getCurrentOrder().getAddedItems().remove(targetLineItem);
                }

            }
            Order.getCurrentOrder().changeTotalPrice();
        }
        Map<String, Object> response = getShoppingCartData();
        System.out.println("response here");
        System.out.println((response.get("shoppingCart")));
        return Utils.toJson(response);
    }

    private static Map<String, Object> getShoppingCartData() {
        ModelBuilder modelBuilder = ModelBuilder.getInstance();
        System.out.println("itt még van");
        List<LineItem> orderItems = Order.getCurrentOrder().getAddedItems();
        System.out.println(orderItems);
        List<Map> orders = modelBuilder.lineItemModel(orderItems);
        System.out.println(orders);
        Map<String, Object> response = new HashMap<>();
        response.put("itemsNumber", Integer.toString(Order.getCurrentOrder().getTotalSize()));
        response.put("totalPrice", Float.toString(Order.getCurrentOrder().getTotalPrice()));
        response.put("shoppingCart", orders);

        System.out.println("response");
        System.out.println(orders);
//        System.out.println(response);
        return response;
    }
}
