package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.Db.UserDaoJdbc;
import com.codecool.shop.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static UserDao userDaoDb = UserDaoJdbc.getInstance();
    private static Utils utils = Utils.getInstance();

    public static String renderLogin(Request req, Response res) {
        Map model = new HashMap();

        if (req.session().attribute("message") != null) {
            String message = req.session().attribute("message");
            model.put("message", message);
            req.session().removeAttribute("message");
        }

        return utils.renderTemplate(model, "product/login");
    }

    public static String registration(Request req, Response res) {
        String saltedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
        User user = new User(req.queryParams("name"), req.queryParams("email"), saltedPassword);
        boolean success = userDaoDb.add(user);

        if (success) {

            LOGGER.info("Registration successful");

            EmailSender emailSender = new EmailSender(user.getEmail());
            emailSender.send();
            req.session().attribute("username", user.getName());
            res.redirect("/");
        } else {
            System.out.println("Username already in use");
            req.session().attribute("message", "Username already in use.");
            res.redirect("/login");
        }

        return "";
    }

    public static String login(Request req, Response res) {
        User user = new User(req.queryParams("name"), req.queryParams("password"));
        User selectedUser = userDaoDb.find(req.queryParams("name"));

        LOGGER.debug("Findig user based on request returned username: {}", selectedUser);

        if( selectedUser != null ){
            if (BCrypt.checkpw(user.getPassword(), selectedUser.getPassword())) {

                LOGGER.info("Password matches after check");

                req.session().attribute("username", selectedUser.getName());
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

        return "";
    }

    public static String logOut(Request req, Response res) {
        req.session().removeAttribute("username");
        res.redirect("/login");

        LOGGER.info("Successfully logged out");

        return "";
    }

    public static void ensureUserIsLoggedIn(Request req, Response res) {

        LOGGER.info("User is not logged in, redirecting to login page");

        if (req.session().attribute("username") == null) {
            res.redirect("/login");
        }
    }
}
