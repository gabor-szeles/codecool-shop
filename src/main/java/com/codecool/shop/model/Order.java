package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order extends BaseModel {
    private static Order currentOrder;
    protected int id;
    private List<LineItem> addedItems;
    private float totalPrice;
    private int totalSize;
    private Map<String, String> userData;
    private Map<String, String> paymentData;
    private int userId;

    public Order(int userId) {
        super("User Order");
        this.addedItems = new ArrayList<>();
        totalPrice = 0;
        totalSize = 0;
        currentOrder = this;
        this.userId = userId;
    }

    public Order(List<LineItem> lineItems, int userId) {
        super("User Order");
        this.addedItems = lineItems;
        this.totalPrice = calculateTotalPrice(lineItems);
        this.totalSize = lineItems.size();
        currentOrder = this;
        this.userId = userId;
    }

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public void add(LineItem lineItem) {
        this.addedItems.add(lineItem);
        addItem(lineItem);

    }

    public void addItem(LineItem lineItem) {
        this.totalPrice += lineItem.getItemPriceSum();
    }

    public List<LineItem> getAddedItems() {
        return addedItems;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void changeTotalPrice() {
        totalPrice = 0;
        for (LineItem lineItem : this.addedItems) {
            totalPrice += lineItem.getItemPriceSum();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void incrementTotalSize() {
        this.totalSize++;
    }

    public void decrementTotalSize() {
        this.totalSize--;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "id: " + id + ", addedItems: " + addedItems + ", totalPrice :" + totalPrice;
    }

    public Map<String, String> getUserData() {
        return userData;
    }

    public void setUserData(Map<String, String> userData) {
        this.userData = userData;
    }

    public Map<String, String> getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(Map<String, String> paymentData) {
        this.paymentData = paymentData;
    }

    private float calculateTotalPrice(List<LineItem> lineItems) {
        float totalPrice = 0;
        for (LineItem lineItem : lineItems) {
            totalPrice += lineItem.getItemPriceSum();
        }
        return totalPrice;
    }
}
