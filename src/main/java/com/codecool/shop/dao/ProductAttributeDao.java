package com.codecool.shop.dao;

import java.util.List;

public interface ProductAttributeDao<T> {

    T find(int id);
    List<T> getAll();
}
