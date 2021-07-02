package com.carrot.jdbc;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Logger;

public class Driver implements java.sql.Driver {

    static{
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        try {
            info.setProperty("port","9527");
            return new com.carrot.jdbc.Connection(url,info);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

}
