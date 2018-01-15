package com.codecool.shop.dao.implementation.Mem;

import com.codecool.shop.dao.BaseDao;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;

/**
 * SupplierDaoMem provides access to Supplier objects in the memory
 */
public class SupplierDaoMem implements BaseDao<Supplier> {

    private static SupplierDaoMem instance = null;
    private List<Supplier> DATA = new ArrayList<>();

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoMem() {
    }

    /**
     * Returns the data access object for memory
     */
    public static SupplierDaoMem getInstance() {
        if (instance == null) {
            instance = new SupplierDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        supplier.setId(DATA.size() + 1);
        DATA.add(supplier);
    }

    @Override
    public Supplier find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }

    @Override
    public List<Supplier> getAll() {
        return DATA;
    }

    /**
     * Clears the list of Supplier objects
     */
    public void clear() {
        DATA.clear();
    }
}
