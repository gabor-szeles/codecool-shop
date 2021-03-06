package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.Validator;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.Db.UserDaoJdbc;
import com.codecool.shop.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.rmi.CORBA.Util;
import java.util.HashMap;
import java.util.Map;

/**
 * Usercontroller is a class responsible to handle user session and verification.
 */
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static UserDao userDaoDb = UserDaoJdbc.getInstance();
    private static Utils utils = Utils.getInstance();
    private static Validator validator = Validator.getInstance();

    /**
     * This method checks if a user is already logged in, if not redirects to the login page, otherwise establishes
     * a session.
     * @param req request sent from client side
     * @param res response sent after processing the request
     * @return empty string required for thymeleaf
     */
    public static String renderLogin(Request req, Response res) {
        Map model = new HashMap();

        if (req.session().attribute("message") != null) {
            String message = req.session().attribute("message");
            model.put("message", message);
            req.session().removeAttribute("message");
        }

        return utils.renderTemplate(model, "product/login");
    }

    /**
     * This method is responsible for registering the users data based on request data. Also updates the database
     * with the new data, including password encryption and confirmation e-mail sending.
     * @param req request sent from client side
     * @param res response sent after processing the request
     * @return empty string required for thymeleaf
     */
    public static String registration(Request req, Response res) {
        Map<String, String> regData = new HashMap<>();
        Map<String, String> response = new HashMap<>();

        regData.put("name", req.queryParams("name"));
        regData.put("password", req.queryParams("password"));
        regData.put("email", req.queryParams("email"));

        if (validator.validateRegistration(regData, response)) {
            String saltedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
            User user = new User(req.queryParams("name"), req.queryParams("email"), saltedPassword);
            Integer userId = userDaoDb.add(user);

            if (userId != null) {

                LOGGER.info("Registration successful");

                EmailSender emailSender = new EmailSender(user.getEmail());
                emailSender.send();
                req.session().attribute("username", user.getName());
                req.session().attribute("userId", userId);

                res.redirect("/");
            } else {
                System.out.println("Username is invalid or already in use");
                req.session().attribute("message", "Username is invalid or already in use.");
                res.redirect("/login");
            }

        } else {
            System.out.println("Registration data is invalid");
            String errorMessage = createErrorMessage(response);
            req.session().attribute("message", errorMessage);
            res.redirect("/login");
        }

        return "";
    }

    private static String createErrorMessage(Map<String, String> response) {
        String errorMessage = "";
        if (response.get("username") != null)  {
            errorMessage += response.get("username") + "\n";
        }
        if (response.get("password") != null) {
            errorMessage += response.get("password") + "\n";
        }
        if (response.get("email") != null) {
            errorMessage += response.get("email") + "\n";
        }
        return errorMessage;
    }

    /**
     * This method is responsible for verifying existing user based on request data, and if the input machs the
     * system lets the user in waiting for further instructions.
     * @param req request sent from client side
     * @param res response sent after processing the request
     * @return empty string required for thymeleaf
     */
    public static String login(Request req, Response res) {

        Map<String, String> loginData = new HashMap<>();
        Map<String, String> response = new HashMap<>();

        loginData.put("name", req.queryParams("name"));
        loginData.put("password", req.queryParams("password"));

        if (validator.validateLogin(loginData, response)) {

            User user = new User(req.queryParams("name"), req.queryParams("password"));
            User selectedUser = userDaoDb.find(req.queryParams("name"));

            LOGGER.debug("Finding user based on request returned username: {}", selectedUser);

            if (selectedUser != null) {
                if (BCrypt.checkpw(user.getPassword(), selectedUser.getPassword())) {

                    LOGGER.info("Password matches after check");
                    req.session().attribute("userId", selectedUser.getId());
                    req.session().attribute("username", selectedUser.getName());
                    OrderController.checkAndCreateOrder(req.session().attribute("userId"));
                    res.redirect("/");
                } else {

                    LOGGER.info("Password matches after check");

                    req.session().attribute("message", "Wrong password.");
                    res.redirect("/login");
                }

            } else {

                LOGGER.debug("Did not find user");

                req.session().attribute("message", "User doesn't exist.");
                res.redirect("/login");
            }

        } else {
            String errorMessage = createErrorMessage(response);
            req.session().attribute("message", errorMessage);
            res.redirect("/login");
        }

        return "";
    }

    /**
     * This method responsible for ceasing the session and redirecting to the login page requesting for a new session.
     * @param req request sent from client side
     * @param res response sent after processing the request
     * @return empty string required for thymeleaf
     */
    public static String logOut(Request req, Response res) {
        req.session().removeAttribute("username");
        res.redirect("/login");

        LOGGER.info("Successfully logged out");

        return "";
    }

    /**
     * A check method that returns to the login page if no user is logged in.
     * @param req request sent from client side
     * @param res response sent after processing the request
     */
    public static void ensureUserIsLoggedIn(Request req, Response res) {
        
        LOGGER.info("User is not logged in, redirecting to login page");

        if (req.session().attribute("username") == null) {
            res.redirect("/login");
        }
    }
}
