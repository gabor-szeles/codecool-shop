package com.codecool.shop;

import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

public class TestUtils {

    public static void populateData() {

        //setting up a new supplier
        Supplier gardenersSupplyCompany = new Supplier("Gardeners Supply Company", "company providing environmentally friendly gardening products and information through its website, catalogs, and retail stores");
        Supplier captainJacks = new Supplier("Captain Jacks Dead Bug", "Kills bagworms, borers, beetles, caterpillars, codling moth, gypsy moth, loopers, leaf miners, spider mites, tent caterpillars, thrips and more!");

        //setting up a new product category
        ProductCategory pestDiseaseControls = new ProductCategory("Pest & Disease Controls", "Gardneing", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        ProductCategory soilsFertilizers = new ProductCategory("Soils & Fertilizers", "Gardening", "A fertilizer is any material of natural or synthetic origin that is applied to soils or to plant tissues to supply one or more plant nutrients essential to the growth of plants.");

        //setting up products and printing it
        ProductDaoMem.getInstance().add(new Product("Super Hoops", 32.95f, "USD", "Use hoops to support garden row covers, protecting plants from frost, insects, birds, or intense sun.", pestDiseaseControls, gardenersSupplyCompany));
        ProductDaoMem.getInstance().add(new Product("Deadbug Dust", 10.95f, "USD", "Highly Effective Organic Insecticide Spinosad", pestDiseaseControls, captainJacks));
        ProductDaoMem.getInstance().add(new Product("Gopher and Mole Repellers", 9.95f, "USD", "Effective, long-lasting and humane deterrent for gophers and moles", pestDiseaseControls, gardenersSupplyCompany));
        ProductDaoMem.getInstance().add(new Product("Raised Bed Booster Kit", 24.95f, "USD", "Booster Kit revitalizes the soil in your raised beds.", soilsFertilizers, gardenersSupplyCompany));
        ProductDaoMem.getInstance().add(new Product("Organic Tomato Fertilizer", 9.95f, "USD", "Organic fertilizer provides essential nutrients ", soilsFertilizers, gardenersSupplyCompany));
    }
}
