package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.implementation.Db.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.Db.SupplierDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    private static ProductDao testDao;
    private static ProductCategory testCategory;
    private static Supplier testSupplier;

    @BeforeEach
    public void setUpDao() {
        ProductDaoMem.getInstance().clear();
        testDao = ProductDaoJdbc.getInstance();
        testCategory = new ProductCategory("test", "test", "test");
        testCategory.setId(1);
        ProductCategoryDaoJdbc.getInstance().add(testCategory);
        testSupplier = new Supplier("test", "test");
        testSupplier.setId(1);
        SupplierDaoJdbc.getInstance().add(testSupplier);
    }

    @AfterEach
    public void removeInit() {
        ProductCategoryDaoJdbc.getInstance().remove(testCategory.getId());
        SupplierDaoJdbc.getInstance().remove(testSupplier.getId());
    }

    @Test
    public void testFind() {
        assertNull(testDao.find(1));
    }

    @Test
    public void testAdd() {
        Product testProduct = new Product("add", 1, "USD", "test", testCategory, testSupplier);
        testProduct.setId(1);
        testDao.add(testProduct);
        assertNotNull(testDao.find(1));
        testDao.remove(testProduct.getId());
    }

    @Test
    public void testRemove() {
        Product testProduct = new Product("rem", 1, "USD", "test", testCategory, testSupplier);
        testProduct.setId(1);
        testDao.add(testProduct);
        assertNotNull(testDao.find(1));
        testDao.remove(testProduct.getId());
        assertNull(testDao.find(1));
    }

    @Test
    public void testGetAll() {
        Product testProduct = new Product("all", 1, "USD", "test", testCategory, testSupplier);
        testProduct.setId(1);
        testDao.add(testProduct);
        List<Product> testProductList = testDao.getAll();
        assertEquals(1, testProductList.size());
        testDao.remove(1);
    }

    @Test
    public void testGetBySupplier() {
        Product testProduct = new Product("supptest", 1, "USD", "test", testCategory, testSupplier);
        testProduct.setId(1);
        testDao.add(testProduct);
        List<Product> testProductListBySupplier = testDao.getBy(testSupplier);
        assertEquals(1, testProductListBySupplier.size());
        testDao.remove(1);
    }

    @Test
    public void testGetByCategory() {
        Product testProduct = new Product("categorytest", 1, "USD", "test", testCategory, testSupplier);
        testProduct.setId(1);
        testDao.add(testProduct);
        List<Product> testProductListByCategory = testDao.getBy(testCategory);
        assertEquals(1, testProductListByCategory.size());
        testDao.remove(1);
    }

}