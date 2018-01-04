package com.codecool.shop.dao.implementation.Db;

import com.codecool.shop.Db_handler;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoJdbc implements UserDao {

    private static Db_handler db_handler = Db_handler.getInstance();
    private static UserDaoJdbc instance = null;

    private UserDaoJdbc() {}

    public static UserDaoJdbc getInstance() {
        if (instance == null) {
            instance = new UserDaoJdbc();
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
        String query = "SELECT name, password FROM users WHERE name = ?";

        try {
            Connection connection = db_handler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("password"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
