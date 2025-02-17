package com.inventorymanagementsystem.nea.ims;

import java.sql.*;

public class User {
    private static String username;
    private static String name;


    public static String setCurrentUser(String username) throws SQLException {
        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        Connection connection = DriverManager.getConnection(url);
        // Sets up SQL connection

        PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM users WHERE LOWER(username) = LOWER(?)");
        getStatement.setString(1, username);
        ResultSet results = getStatement.executeQuery();
        // Performs query

        if (results.next()){
            name = results.getString("name");
            User.username = username;
            connection.close();
            return "";
        }
        else {
            connection.close();
            return "No such user exists!";
        }
    }

    // Getters / Setters
    public static String getUsername(){
        return username;
    }

    public static String getName(){
        return name;
    }


}
