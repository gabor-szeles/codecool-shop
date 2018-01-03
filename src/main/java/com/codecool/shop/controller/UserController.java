package com.codecool.shop.controller;

import com.codecool.shop.Utils;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.Db.UserDaoDb;
import com.codecool.shop.model.User;
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
        User user = new User(req.queryParams("name"), req.queryParams("password"));
        boolean success = userDaoDb.add(user);

        if (success) {
            res.redirect("/");
        } else {
            res.redirect("/login");
        }

        return "";
    }
}
