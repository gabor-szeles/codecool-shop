package com.codecool.shop.model;

public class LineItem {

    Product item;
    int quantity;
    int itemPriceSum;

    LineItem(Product item, int price) {
        this.item = item;
        this.quantity = 1;
        this.itemPriceSum = price;
    }


}
