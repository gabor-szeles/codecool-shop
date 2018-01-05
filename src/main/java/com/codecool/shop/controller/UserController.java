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

public class UserController {

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

    public static String logOut(Request req, Response res) {
        req.session().removeAttribute("username");
        res.redirect("/login");

        return "";
    }

    public static void ensureUserIsLoggedIn(Request req, Response res) {
        if (req.session().attribute("username") == null) {
            res.redirect("/login");
        }
    }
}
