package com.inventorymanagementsystem.nea.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


public class SignUpController extends DefaultController {
    @FXML
    private Label errorLbl;
    @FXML
    private TextField userField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passField;
    @FXML
    private PasswordField cPassField;
    @FXML
    private Button signupBtn;
    @FXML
    private Button loginBtn;


    public void switchToLogin(ActionEvent event){
        try {
            switchToScene(event, "login.fxml", new String[]{"styles/signup_login.css"},"IMS - log in" );
        } catch (IOException e) {
            System.out.println("Change scene caused error");
            // TODO: when errorPopup is created, add one here
        }
    }

    public void signup(ActionEvent event){
        try {
            String username = userField.getText();
            String name = nameField.getText();
            String password = passField.getText();
            String confPassword = cPassField.getText();

            if (Validator.username(username) && Validator.password(password) && Validator.username(name)) {
                if (!password.equals(confPassword)){
                    throw new IllegalArgumentException("Passwords do not match!");
                }
                createAccount(username,name,password);
                successPopup("Sign Up Successful!", "You can now login!");
            }
        } catch (IllegalArgumentException e){
          errorLbl.setText(e.getMessage());
        }
    }

    public void createAccount(String username, String name, String password) {
        try {
            String url = "jdbc:sqlite:C:\\Users\\s230379\\OneDrive - Greenhead College\\Documents\\GitHub\\InventoryManagementSystem\\Source_code\\src\\main\\resources\\com\\inventorymanagementsystem\\nea\\ims\\SQLdb\\IMS_database";
            Connection connection = DriverManager.getConnection(url);
            // Sets up SQL connection

            PreparedStatement getStatement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            getStatement.setString(1, username);
            ResultSet results = getStatement.executeQuery();
            // Performs query

            if (results.next() && results.getInt(1) != 0) {
                errorLbl.setText("Sorry, username already taken!");
                return;
            }

            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?)");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            // Hashes password

            insertStatement.setString(1, username);
            insertStatement.setString(2, name);
            insertStatement.setBytes(3, hash);
            insertStatement.executeUpdate();
            // Sets parameters of prepared statement and runs it

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
