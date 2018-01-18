package com.codecool.shop.model;

import sun.misc.Request;

public class LineItem {

    private int id;
    private Product item;
    private int quantity;
    private float itemPriceSum;

    public LineItem(Product item, float price, int userId) {
        this.item = item;
        this.id = item.getId();
        this.quantity = 1;
        this.itemPriceSum = price;
        Order.getActiveOrder(userId).incrementTotalSize();
    }

    public LineItem(Product item, int quantity) {
        this.item = item;
        this.id = item.getId();
        this.quantity = quantity;
        this.itemPriceSum = calculateTotalPrice(item, quantity);
    }

    public float getItemPriceSum() {
        return itemPriceSum;
    }


    @Override
    public String toString() {
        return "item: " + item.getName() +
                ", quantity: " + quantity +
                ", itemPriceSum: " + itemPriceSum;
    }

    public Product getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void incrementQuantity(int userId) {
        this.quantity++;
        this.itemPriceSum = this.item.getDefaultPrice() * quantity;
        Order.getActiveOrder(userId).changeTotalPrice();
        Order.getActiveOrder(userId).incrementTotalSize();
    }

    public void decrementQuantity(int userId) {
        this.quantity--;
        this.itemPriceSum = this.item.getDefaultPrice() * quantity;
        Order.getActiveOrder(userId).changeTotalPrice();
        Order.getActiveOrder(userId).decrementTotalSize();
    }

    private float calculateTotalPrice(Product product, int quantity) {
        return product.getDefaultPrice()*quantity;
    }
}
