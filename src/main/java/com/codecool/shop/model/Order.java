package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    protected int id;
    private List<LineItem> addedItems;
    private float totalPrice;
    private static Order currentOrder;

    public Order() {
        this.addedItems = new ArrayList<>();
        totalPrice = 0;
        currentOrder = this;
    }

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public void add (LineItem lineItem) {
        this.addedItems.add(lineItem);
        this.totalPrice += lineItem.getItemPriceSum();
    }

    public List<LineItem> getAddedItems() {
        return addedItems;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
