package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoDb implements UserDao {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static UserDaoDb instance = null;

    private UserDaoDb() {}

    public static UserDaoDb getInstance() {
        if (instance == null) {
            instance = new UserDaoDb();
        }
        return instance;
    }

    @Override
    public boolean add(User user) {
        String query = "INSERT INTO users (name, password) VALUES (?, ?)";

        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }

    @Override
    public User find(String username) {
        return null;
    }
}
