package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    List<LineItem> addedItems;
    int totalPrice;

    Order() {
        this.addedItems = new ArrayList<>();
        totalPrice = 0;
    }

}
