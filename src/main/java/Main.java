import com.codecool.shop.controller.ProductController;
import com.codecool.shop.controller.UserController;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.impl.SimpleLogger;

import java.util.Arrays;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    public static void main(String[] args) {

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd HH:mm:ss");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");

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
    }

}
