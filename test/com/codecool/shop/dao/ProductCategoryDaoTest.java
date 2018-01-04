package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductCategoryDaoTest {

    private static ProductCategoryDao testProductCategoryDao;


    @BeforeEach
    public void setUpDao() {
        ProductCategoryDaoMem.getInstance().clear();
        testProductCategoryDao = ProductCategoryDaoJdbc.getInstance();
    }

    @Test
    public void testAdd () {
        ProductCategory testProductCategory = new ProductCategory("add",
                "testProductCategoryDepartment",
                "testProductCategoryDescription" );
        testProductCategory.setId(1);
        testProductCategoryDao.add(testProductCategory);
        assertEquals("add", testProductCategoryDao.find(1).getName());
        testProductCategoryDao.remove(1);
    }

    @Test
    public void testFind () {
        ProductCategory testProductCategory = new ProductCategory("find",
                "testProductCategoryDepartment",
                "testProductCategoryDescription" );
        testProductCategoryDao.add(testProductCategory);
        System.out.println(testProductCategoryDao.getAll());
        ProductCategory expectedProductCategory = testProductCategoryDao.find(1);
        assertEquals(expectedProductCategory.getName(), "find");
        testProductCategoryDao.remove(1);
    }

    @Test
    public void testRemove () {
        ProductCategory testProductCategory = new ProductCategory("rm",
                "testProductCategoryDepartment",
                "testProductCategoryDescription" );
        testProductCategoryDao.add(testProductCategory);
        testProductCategoryDao.remove(0);
        assertNull(testProductCategoryDao.find(0));
    }

    @Test
    public void testGetAll () {
        List <ProductCategory> testProductCategoryList = testProductCategoryDao.getAll();
        assertEquals(1, testProductCategoryList.size());
    }
}
