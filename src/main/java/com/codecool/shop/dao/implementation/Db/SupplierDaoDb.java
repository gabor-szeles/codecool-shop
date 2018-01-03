package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
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

        String query = "SELECT * FROM supplier WHERE id = ?;";

        ResultSet foundElement = db_handler.createPreparedStatementForFindOrRemove(id, query);
        try {
            foundElement.next();
            Supplier foundSupplier = new Supplier(foundElement.getString("name"), foundElement.getString("description"));
            foundSupplier.setId(foundElement.getInt("id"));
            return foundSupplier;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {

        String query = "DELETE FROM supplier WHERE id = ?;";

        db_handler.createPreparedStatementForFindOrRemove(id, query);

    }

    @Override
    public List<Supplier> getAll() {
        return new ArrayList<>();
    }
}
