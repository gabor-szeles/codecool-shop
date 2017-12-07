package com.codecool.shop.model;

public class LineItem {

    Product item;
    int quantity;
    float itemPriceSum;

    public LineItem(Product item, float price) {
        this.item = item;
        this.quantity = 1;
        this.itemPriceSum = price;
        Order.getCurrentOrder().incrementTotalSize();
    }

    public float getItemPriceSum() {
        return itemPriceSum;
    }


    @Override
    public String toString() {
        return  "item: " + item.getName() +
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
        this.itemPriceSum += this.item.getDefaultPrice();
        Order.getCurrentOrder().incrementTotalPrice(this.item.getDefaultPrice());
        Order.getCurrentOrder().incrementTotalSize();
    }

    public void decrementQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
            this.itemPriceSum -= this.item.getDefaultPrice();
            Order.getCurrentOrder().decrementTotalPrice(this.item.getDefaultPrice());
            Order.getCurrentOrder().decrementTotalSize();
        }
    }
}
