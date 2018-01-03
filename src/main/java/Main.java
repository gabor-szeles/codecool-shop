import com.codecool.shop.controller.ProductController;
import com.codecool.shop.controller.UserController;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.Arrays;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    public static void main(String[] args) {

        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFiles.location("/public");
        port(5000);

        before((request, response) -> {
            System.out.println(request.requestMethod() + " @ " + request.url());
            // check if user logged in
            // put data to localStorage
        });

        // populate some data for the memory storage
        populateData();

        // routes
        get("/", ProductController::renderProducts);

        get("/api/get-supplier-products/:id", ProductController::getProductsBySupplier);

        get("/api/get-category-products/:id", ProductController::getProductsByCategory);

        get("/api/add-product/:id", ProductController::handleOrder);

        post("/api/change-quantity/", ProductController::changeQuantity);

        post("/api/add-user-data", ProductController::addUserData);

        post("/api/add-credit-card-data", ProductController::addPaymentData);

        post("/api/add-pay-pal-data", ProductController::addPaymentData);

        get("/login", UserController::renderLogin);

        post("/api/user/registration", UserController::registration);

        post("/api/user/login", UserController::login);

        get("/api/user/logout", UserController::logOut);

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    private static void populateData() {
        Order shoppingCart = new Order();


        //setting up a new supplier
        Supplier gardenersSupplyCompany = new Supplier("Gardeners Supply Company", "company providing environmentally friendly gardening products and information through its website, catalogs, and retail stores");
        Supplier captainJacks = new Supplier("Captain Jacks Dead Bug", "Kills bagworms, borers, beetles, caterpillars, codling moth, gypsy moth, loopers, leaf miners, spider mites, tent caterpillars, thrips and more!");

        //setting up a new product category
        ProductCategory pestDiseaseControls = new ProductCategory("Pest & Disease Controls", "Gardneing", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        ProductCategory soilsFertilizers = new ProductCategory("Soils & Fertilizers", "Gardening", "A fertilizer is any material of natural or synthetic origin that is applied to soils or to plant tissues to supply one or more plant nutrients essential to the growth of plants.");

        //setting up products and printing it
        new Product("Super Hoops", 32.95f, "USD", "Use hoops to support garden row covers, protecting plants from frost, insects, birds, or intense sun.", pestDiseaseControls, gardenersSupplyCompany);
        new Product("Deadbug Dust", 10.95f, "USD", "Highly Effective Organic Insecticide Spinosad", pestDiseaseControls, captainJacks);
        new Product("Gopher and Mole Repellers", 9.95f, "USD", "Effective, long-lasting and humane deterrent for gophers and moles", pestDiseaseControls, gardenersSupplyCompany);
        new Product("Raised Bed Booster Kit", 24.95f, "USD", "Booster Kit revitalizes the soil in your raised beds.", soilsFertilizers, gardenersSupplyCompany);
        new Product("Organic Tomato Fertilizer", 9.95f, "USD", "Organic fertilizer provides essential nutrients ", soilsFertilizers, gardenersSupplyCompany);
    }

}
