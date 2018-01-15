package com.codecool.shop.dao;

import com.codecool.shop.model.Supplier;

import java.util.List;

/**
 * SupplierDao is the interface providing access to Supplier data in the memory/database.
 */
public interface SupplierDao {

    /**
     * Adds a Supplier object to the memory or a record to the database
     * @param supplier Supplier object to be added
     */
    void add(Supplier supplier);

    /**
     * Finds a supplier in the memory/database by its id
     * @param id The id of the supplier to be found
     * @return Returns the Supplier object found
     */
    Supplier find(int id);

    /**
     * Removes a supplier from the memory/database by its id
     * @param id The id of the supplier to be removed
     */
    void remove(int id);

    /**
     * Returns a List of every Supplier found in the memory or the database
     * @return A list of Supplier objects
     */
    List<Supplier> getAll();
}
