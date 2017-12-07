package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.OrderDaoMem;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;

public class Order {
    protected int id;
    private List<LineItem> addedItems;
    private float totalPrice;
    private static Order currentOrder;
    private int totalSize;

    public Order() {
        OrderDaoMem orderData = OrderDaoMem.getInstance();
        this.addedItems = new ArrayList<>();
        totalPrice = 0;
        totalSize = 0;
        orderData.add(this);
        currentOrder = this;
    }

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public void add (LineItem lineItem) {
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

    public void incrementTotalPrice(float price) {
        this.totalPrice += price;
    }

    public void decrementTotalPrice(float price) {
        this.totalPrice -= price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "id: " + id + ", addedItems: " + addedItems + ", totalPrice :" + totalPrice;
    }
}
