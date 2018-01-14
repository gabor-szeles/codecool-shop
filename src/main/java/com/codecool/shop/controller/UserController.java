package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.Db.UserDaoJdbc;
import com.codecool.shop.model.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Usercontroller is a class responsible to handle user session and verification.
 */
public class UserController {

    private static UserDao userDaoDb = UserDaoJdbc.getInstance();
    private static Utils utils = Utils.getInstance();

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
        String saltedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
        User user = new User(req.queryParams("name"), req.queryParams("email"), saltedPassword);
        boolean success = userDaoDb.add(user);

        if (success) {
            System.out.println("Registration successful");
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

    /**
     * This method is responsible for verifying existing user based on request data, and if the input machs the
     * system lets the user in waiting for further instructions.
     * @param req request sent from client side
     * @param res response sent after processing the request
     * @return empty string required for thymeleaf
     */
    public static String login(Request req, Response res) {
        User user = new User(req.queryParams("name"), req.queryParams("password"));
        User selectedUser = userDaoDb.find(req.queryParams("name"));
        if( selectedUser != null ){
            if (BCrypt.checkpw(user.getPassword(), selectedUser.getPassword())) {
                System.out.println("Password matches");
                req.session().attribute("username", selectedUser.getName());
                res.redirect("/");
            } else {
                System.out.println("Password doesn't match");
                req.session().attribute("message", "Wrong password.");
                res.redirect("/login");
            }
        } else {
            System.out.println("User doesn't exist");
            req.session().attribute("message", "User doesn't exist.");
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

        return "";
    }

    /**
     * A check method that returns to the login page if no user is logged in.
     * @param req request sent from client side
     * @param res response sent after processing the request
     */
    public static void ensureUserIsLoggedIn(Request req, Response res) {
        if (req.session().attribute("username") == null) {
            res.redirect("/login");
        }
    }
}
