package com.revplay.util;

import com.revplay.config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    public static Connection getConnection() throws Exception {

        Connection con = DriverManager.getConnection(
                DBConfig.URL,
                DBConfig.USERNAME,
                DBConfig.PASSWORD
        );

        System.out.println("Database connection successful");
        return con;
    }
}
