package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.dao.implementation.Mem.SupplierDaoMem;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJdbc implements SupplierDao {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static SupplierDaoJdbc instance = null;
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoJdbc.class);

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoJdbc() {
    }

    public static SupplierDaoJdbc getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJdbc();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        String query = "INSERT INTO supplier (id, name, description) " +
                "VALUES (?,?,?);";
        logger.debug("Supplier add query created");
        db_handler.createPreparedStatementForAdd(supplier, query);
    }

    @Override
    public Supplier find(int id) {
        SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();

        if (supplierDaoMem.getAll().contains(supplierDaoMem.find(id))) {
            logger.debug("Memory contains Supplier id {}", id);
            return supplierDaoMem.find(id);
        } else {

            String query = "SELECT * FROM supplier WHERE id = ?;";

            ResultSet foundElement = db_handler.createPreparedStatementForFind(id, query);
            try {
                foundElement.next();
                Supplier foundSupplier = new Supplier(foundElement.getString("name"), foundElement.getString("description"));
                foundSupplier.setId(foundElement.getInt("id"));
                supplierDaoMem.add(foundSupplier);
                logger.debug("Supplier {} added to ProductDaoMem", foundSupplier.getName());
                return foundSupplier;
            } catch (SQLException e) {
                logger.warn("No SQL entry found for supplier id {}", id);
                return null;
            }
        }
    }

    @Override
    public void remove(int id) {
        SupplierDaoMem.getInstance().remove(id);
        logger.debug("Supplier id {} removed from DaoMem", id);
        String query = "DELETE FROM supplier WHERE id = ?;";
        db_handler.createPreparedStatementForRemove(id, query);
    }

    @Override
    public List<Supplier> getAll() {
        SupplierDaoMem.getInstance().clear();
        ArrayList<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM supplier";
        ResultSet foundElements = db_handler.createPreparedStatementForGetAll(query);

        try {
            while (foundElements.next()){
                Supplier newSupplier = new Supplier(foundElements.getString("name"),
                        foundElements.getString("description"));
                newSupplier.setId(foundElements.getInt("id"));
                SupplierDaoMem.getInstance().add(newSupplier);
                suppliers.add(newSupplier);
            }
        } catch (SQLException e) {
            logger.warn("Supplier table empty!");
            e.printStackTrace();
        }

        logger.debug("{} suppliers found", suppliers.size());
        return suppliers;
    }
}
