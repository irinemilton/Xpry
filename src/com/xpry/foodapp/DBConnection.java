package com.xpry.foodapp;


//DBConnection.java - MySQL Connection Setup
import java.sql.*;
public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/xpry_db";
        String user = "root";
        String password = "ROOT";
        return DriverManager.getConnection(url, user, password);
    }
}
