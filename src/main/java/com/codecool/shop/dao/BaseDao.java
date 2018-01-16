package com.codecool.shop.dao;

import com.codecool.shop.model.BaseModel;

import java.util.List;

public interface BaseDao<T extends BaseModel> {

    void add(T model);

    T find(int id);

    void remove(int id);

    List<T> getAll();


}
