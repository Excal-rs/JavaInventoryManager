package com.inventorymanagementsystem.nea.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;

public class LogInController extends DefaultController{
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Label errorLbl;

    public void login(ActionEvent event) throws SQLException, NoSuchAlgorithmException {
        String username = userField.getText();
        String password = passField.getText();

        ValidationResult usernameCheck = Validator.username(username);
        ValidationResult passwordCheck = Validator.password(password);

        if (!usernameCheck.isValid() || !passwordCheck.isValid()){
            if (!usernameCheck.isValid()){
                errorLbl.setText(usernameCheck.getReason());
            }
            else {
                errorLbl.setText(passwordCheck.getReason());
            }
        }
        else {
            String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
            Connection connection = DriverManager.getConnection(url);
            // Sets up SQL connection

            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM users WHERE LOWER(username) = LOWER(?)");
            getStatement.setString(1, username);
            ResultSet results = getStatement.executeQuery();
            // Performs query

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            // Hashes given password

            if (results.next()) {
                byte[] actualHash = results.getBytes("hashedPassword");
                if (!Arrays.equals(hash, actualHash)) {
                    errorLbl.setText("Incorrect username or password!");
                    connection.close();
                    return;
                }
                User.setCurrentUser(results.getString("username"));
                successPopup("Login Successful", User.getUsername()); // TODO: in phase 2 update to redirect to dashboard
            } else {
                errorLbl.setText("User does not exist!");
            }
            connection.close();
        }
    }

    public void switchToSignup(ActionEvent event){
        try {
            switchToScene(event, "signup.fxml", new String[]{"styles/signup_login.css"},"IMS - Sign Up" );
        } catch (IOException e) {
            System.out.println("Change scene caused error");
        }
    }
}
