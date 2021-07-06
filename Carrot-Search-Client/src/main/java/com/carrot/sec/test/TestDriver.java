package com.carrot.sec.test;

import java.sql.*;

public class TestDriver {

    static{
        try {
            Class<?> aClass = Class.forName("com.carrot.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:carrot-search://127.0.0.1:9527/temp", "username", "password");
        System.out.println(connection);
        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery();
    }

}
