package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
import com.codecool.shop.model.BaseModel;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductCategoryDaoTest {

    private static BaseDao testProductCategoryDao;


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
        assertEquals("add", testProductCategoryDao.find(testProductCategory.getId()).getName());
        testProductCategoryDao.remove(testProductCategory.getId());
    }

    @Test
    public void testFind () {
        ProductCategory testProductCategory = new ProductCategory("find",
                "testProductCategoryDepartment",
                "testProductCategoryDescription" );
        testProductCategory.setId(1);
        testProductCategoryDao.add(testProductCategory);
        BaseModel expectedProductCategory = testProductCategoryDao.find(testProductCategory.getId());
        assertEquals(expectedProductCategory.getName(), "find");
        testProductCategoryDao.remove(testProductCategory.getId());
    }

    @Test
    public void testRemove () {
        ProductCategory testProductCategory = new ProductCategory("rm",
                "testProductCategoryDepartment",
                "testProductCategoryDescription" );
        testProductCategory.setId(1);
        testProductCategoryDao.add(testProductCategory);
        testProductCategoryDao.remove(testProductCategory.getId());
        assertNull(testProductCategoryDao.find(testProductCategory.getId()));
    }

    @Test
    public void testGetAll () {
        ProductCategory testProductCategory = new ProductCategory("rm",
                "testProductCategoryDepartment",
                "testProductCategoryDescription" );
        testProductCategory.setId(1);
        testProductCategoryDao.add(testProductCategory);
        List <ProductCategory> testProductCategoryList = testProductCategoryDao.getAll();
        assertEquals(1, testProductCategoryList.size());
        testProductCategoryDao.remove(testProductCategory.getId());
    }
}
