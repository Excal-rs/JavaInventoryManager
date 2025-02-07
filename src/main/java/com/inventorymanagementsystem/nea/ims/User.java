package com.inventorymanagementsystem.nea.ims;

import java.sql.*;

public class User {
    private String username;
    private String name;

    public void setCurrentUser(String username) throws SQLException {
        String url = "jdbc:sqlite:C:\\Users\\s230379\\OneDrive - Greenhead College\\Documents\\GitHub\\InventoryManagementSystem\\Source_code\\src\\main\\resources\\com\\inventorymanagementsystem\\nea\\ims\\SQLdb\\IMS_database";
        Connection connection = DriverManager.getConnection(url);
        // Sets up SQL connection

        PreparedStatement getStatement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
        getStatement.setString(1, username);
        ResultSet results = getStatement.executeQuery();
        // Performs query

        if (results.next()){
            String name = results.getString("name");
        }
        else {
            throw new IllegalArgumentException("No such user exists!");
        }

        this.username = username;
        this.name = name;
    }
}
