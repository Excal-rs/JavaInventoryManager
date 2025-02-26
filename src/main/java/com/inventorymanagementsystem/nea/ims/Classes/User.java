package com.inventorymanagementsystem.nea.ims.Classes;

import java.sql.*;

public class User {
    private static String username;
    private static String name;


    public static ValidationResult setCurrentUser(String username) throws SQLException {
        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        Connection connection = DriverManager.getConnection(url);
        // Sets up SQL connection

        PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM users WHERE LOWER(username) = LOWER(?)");
        getStatement.setString(1, username);
        ResultSet results = getStatement.executeQuery();
        // Performs query

        if (results.next()) {
            name = results.getString("name");
            User.username = results.getString("username");
            connection.close();
            return new ValidationResult(true);
        } else {
            connection.close();
            return new ValidationResult(false, "No such user exists!");
        }
    }

    // Getters / Setters
    public static String getUsername() {
        return username;
    }

    public static String getName() {
        return name;
    }


}
