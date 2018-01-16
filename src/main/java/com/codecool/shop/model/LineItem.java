package com.codecool.shop.model;

import java.util.List;

public class LineItem {

    int id;
    Product item;
    int quantity;
    float itemPriceSum;

    public LineItem(Product item, float price) {
        this.item = item;
        this.id = item.getId();
        this.quantity = 1;
        this.itemPriceSum = price;
        Order.getCurrentOrder().incrementTotalSize();
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

    public void incrementQuantity() {
        this.quantity++;
        this.itemPriceSum = this.item.getDefaultPrice() * quantity;
        Order.getCurrentOrder().changeTotalPrice();
        Order.getCurrentOrder().incrementTotalSize();
    }

    public void decrementQuantity() {
        this.quantity--;
        this.itemPriceSum = this.item.getDefaultPrice() * quantity;
        Order.getCurrentOrder().changeTotalPrice();
        Order.getCurrentOrder().decrementTotalSize();
    }

    private float calculateTotalPrice(Product product, int quantity) {
        return product.getDefaultPrice()*quantity;
    }
}
