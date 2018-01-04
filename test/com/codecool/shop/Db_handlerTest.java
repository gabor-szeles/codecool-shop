package com.codecool.shop;

import com.codecool.shop.dao.implementation.Db.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.implementation.Db.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.Db.SupplierDaoJdbc;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


// all in one test file, need to be rewritten asap //


class Db_handlerTest {

    @Test
    void testCreatePreparedStatementForAddSupplier() {
        Supplier supplier = new Supplier("Test", "Test description");
        Db_handler db_handler = Db_handler.getInstance();
        String query = "INSERT INTO supplier (id, name, description) " +
                "VALUES (?,?,?);";
        db_handler.createPreparedStatementForAdd(supplier, query);
    }

    @Test
    void testCreatePreparedStatementForAddProductCategory() {
        ProductCategory productCategory = new ProductCategory("test", "testdep", "test");
        ProductCategoryDaoJdbc productCategoryDaoJdbc = ProductCategoryDaoJdbc.getInstance();
        productCategoryDaoJdbc.add(productCategory);
    }

    @Test
    void testCreatePreparedStatementForAddProduct() {
        Supplier supplier = new Supplier("Test", "Test description");
        ProductCategory productCategory = new ProductCategory("TestCategory", "Testing", "Test description for prodCategory");
        Product product = new Product("TestProduct", 14f, "EUR", "TestDescription", productCategory, supplier);

        String query = "INSERT INTO product (id, name, description, currency_string, default_price, category_id, supplier_id) " +
                "VALUES (?,?,?,?,?,?,?);";

        Db_handler db_handler = Db_handler.getInstance();
        db_handler.createPreparedStatementForAdd(product, query);

    }

    @Test
    void testFindProduct() {
        ProductDaoJdbc productDaoJdbc = ProductDaoJdbc.getInstance();
        Supplier supplier = new Supplier("Test", "Test description");
        ProductCategory productCategory = new ProductCategory("TestCategory", "Testing", "Test description for prodCategory");
        Product product = new Product("TestProduct", 14f, "EUR", "TestDescription", productCategory, supplier);
        Product found = productDaoJdbc.find(1);
        assertEquals(found.getId(), found.getId());
        assertEquals(product.getName(), found.getName());
        assertEquals(product.getDescription(), found.getDescription());
    }

    @Test
    void testDeleteSupplier() {
        SupplierDaoJdbc supplierDaoJdbc = SupplierDaoJdbc.getInstance();
        supplierDaoJdbc.remove(1);
    }

    @Test
    void testDeleteProduct() {
        ProductDaoJdbc productDaoJdbc = ProductDaoJdbc.getInstance();
        productDaoJdbc.remove(1);
    }

    @Test
    void testDeleteProductCategory() {
        ProductCategoryDaoJdbc.getInstance().remove(1);
    }
}