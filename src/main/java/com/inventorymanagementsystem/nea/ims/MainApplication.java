package com.inventorymanagementsystem.nea.ims;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class MainApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader;
        String title;

        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        Connection connection = DriverManager.getConnection(url);
        // Sets up SQL connection

        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM users");
        // Performs query to check if any users exist in local database
        if (results.next() && results.getInt(1) != 0) {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
            title = "IMS - Log In";
        } else {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("signup.fxml"));
            title = "IMS - Sign Up";
        }
        connection.close();
        // Chooses which form to load

        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(MainApplication.class.getResource("styles/signup_login.css").toExternalForm());

        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        // Displays scene
    }
}