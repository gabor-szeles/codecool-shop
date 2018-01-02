package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierDaoDb implements SupplierDao {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static SupplierDaoDb instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoDb() {
    }

    public static SupplierDaoDb getInstance() {
        if (instance == null) {
            instance = new SupplierDaoDb();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        String query = "INSERT INTO supplier (id, name, description) " +
                "VALUES (?,?,?);";

        db_handler.createPreparedStatementForAdd(supplier, query);
    }

    @Override
    public Supplier find(int id) {
        return new Supplier("Name", "Desc");
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Supplier> getAll() {
        return new ArrayList<>();
    }
}
