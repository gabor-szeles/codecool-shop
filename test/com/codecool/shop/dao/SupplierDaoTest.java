package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.Db.SupplierDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.SupplierDaoMem;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SupplierDaoTest {

    private Supplier testSupplier;
    private SupplierDao testDao;

    @BeforeEach
    public void setUpDao() {
        SupplierDaoMem.getInstance().clear();
        testDao = SupplierDaoMem.getInstance();
    }

    @Test
    public void testFind() {
        assertNull(testDao.find(1));
    }

    @Test
    public void testAdd() {
        testSupplier = new Supplier("add", "test");
        testDao.add(testSupplier);
        assertNotNull(testDao.find(testSupplier.getId()));
    }

    @Test
    public void testRemove() {
        testSupplier = new Supplier("rem", "test");
        testDao.add(testSupplier);
        assertNotNull(testDao.find(testSupplier.getId()));
        testDao.remove(testSupplier.getId());
        assertNull(testDao.find(testSupplier.getId()));
    }

    @Test
    public void testGetAll() {
        testSupplier = new Supplier("all", "test");
        testDao.add(testSupplier);
        List<Supplier> testSupplierList = testDao.getAll();
        assertEquals(1, testSupplierList.size());
        testDao.remove(testSupplier.getId());
    }


}
