package com.codecool.shop.dao;

import com.codecool.shop.TestUtils;
import com.codecool.shop.dao.implementation.Db.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    private static ProductDao testDao;
    private static ProductCategory testCategory;
    private static Supplier testSupplier;

    @BeforeAll
    public static void setUpDao() {
        testDao = ProductDaoJdbc.getInstance();
        TestUtils.populateData();
        testCategory = new ProductCategory("test", "test", "test");
        testSupplier = new Supplier("test", "test");
    }

    @Test
    public void testFind() {
        assertNotNull(testDao.find(1));
    }

    @Test
    public void testAdd() {
        Product testProduct = new Product("add", 1, "USD", "test", testCategory, testSupplier);
        testDao.add(testProduct);
        assertNotNull(testDao.find(testProduct.getId()));
    }

    @Test
    public void testRemove() {
        Product testProduct = new Product("rem", 1, "USD", "test", testCategory, testSupplier);
        testDao.add(testProduct);
        assertNotNull(testDao.find(testProduct.getId()));
        testDao.remove(testProduct.getId());
        assertNull(testDao.find(testProduct.getId()));
    }

    @Test
    public void testGetAll() {
        List<Product> testProductList = testDao.getAll();
        assertEquals(6, testProductList.size());
    }

    @Test
    public void testGetBySupplier() {
        Product testProduct = new Product("supptest", 1, "USD", "test", testCategory, testSupplier);
        testDao.add(testProduct);
        List<Product> testProductListBySupplier = testDao.getBy(testSupplier);
        assertEquals(1, testProductListBySupplier.size());
        testDao.remove(testProduct.getId());
    }

    @Test
    public void testGetByCategory() {
        Product testProduct = new Product("categorytest", 1, "USD", "test", testCategory, testSupplier);
        testDao.add(testProduct);
        List<Product> testProductListByCategory = testDao.getBy(testCategory);
        System.out.println(testProductListByCategory.size());
        assertEquals(1, testProductListByCategory.size());
        testDao.remove(testProduct.getId());
    }
}