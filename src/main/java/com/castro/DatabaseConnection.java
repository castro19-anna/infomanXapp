package com.castro;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private String url = "jdbc:mysql://localhost:3306/test";
    private String user = "castroj";
    private String password = "castroj28";

    public Connection connection;
    public DatabaseConnection() {
        try{
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return connection;
    }

}