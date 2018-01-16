package com.codecool.shop;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConnectionDetails {

    private Connection connection;
    private PreparedStatement statement;

    public ConnectionDetails(Connection connection, PreparedStatement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getStatement() {
        return statement;
    }
}
