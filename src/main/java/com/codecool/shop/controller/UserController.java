package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.Db.UserDaoDb;
import com.codecool.shop.model.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    private static UserDao userDaoDb = UserDaoDb.getInstance();

    public static String renderLogin(Request req, Response res) {
        Utils utils = Utils.getInstance();
        Map model = new HashMap();
        return utils.renderTemplate(model, "product/login");
    }

    public static String registration(Request req, Response res) {
        String saltedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
        User user = new User(req.queryParams("name"), saltedPassword);
        boolean success = userDaoDb.add(user);

        if (success) {
            System.out.println("Registration successful");
            req.session().attribute("username", user.getName());
            res.redirect("/");
        } else {
            System.out.println("Username already in use");
            res.redirect("/login");
        }

        return "";
    }

    public static String login(Request req, Response res) {
        User user = new User(req.queryParams("name"), req.queryParams("password"));
        User selectedUser = userDaoDb.find(req.queryParams("name"));
        if (BCrypt.checkpw(user.getPassword(), selectedUser.getPassword())) {
            System.out.println("Password matches");
            req.session().attribute("username", selectedUser.getName());
            res.redirect("/");
        } else {
            System.out.println("Password doesn't match");
            res.redirect("/login");
        }

        return "";
    }

    public static void ensureUserIsLoggedIn(Request req, Response res) {
        if (req.session().attribute("username") == null) {
            res.redirect("/login");
        }
    }
}
