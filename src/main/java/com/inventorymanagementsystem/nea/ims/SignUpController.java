package com.inventorymanagementsystem.nea.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;


public class SignUpController extends DefaultController {
    @FXML
    private Label errorLbl;
    @FXML
    private TextField userField;
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
            String password = passField.getText();
            String confPassword = cPassField.getText();

            if (Validator.username(username) && Validator.password(password)) {
                if (!password.equals(confPassword)){
                    throw new IllegalArgumentException("Passwords do not match!");
                }
                createAccount(username, password);
                successPopup("Sign Up Successful!", "You can now login!");
            }
        } catch (IllegalArgumentException e){
          errorLbl.setText(e.getMessage());
        }
    }

    private void createAccount(String username, String password) throws SQLException {
        String url = "jdbc:sqlite:/SQLdb/IMS_database";
        Connection connection = DriverManager.getConnection(url);
        PreparedStatement getStatement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
        getStatement.setString(1, username);
        ResultSet results = getStatement.executeQuery();

        if (results.next() && results.getInt(1) != 0){
            errorLbl.setText("Sorry, username already taken!");
            return;
        }

        PreparedStatement insertStatement =connection.prepareStatement("INSERT ")

    }

}
