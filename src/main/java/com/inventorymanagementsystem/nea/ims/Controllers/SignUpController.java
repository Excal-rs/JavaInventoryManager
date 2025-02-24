package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.ValidationResult;
import com.inventorymanagementsystem.nea.ims.Classes.Validator;
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

    public void switchToLogin(ActionEvent event) {
        try {
            switchToScene(event, "login.fxml", new String[]{"signupLogin.css"}, "IMS - Log In");
        } catch (IOException e) {
            System.out.println("Change scene caused error");
        }
    }

    public void signup(ActionEvent event) {
        try {
            String username = userField.getText();
            String name = nameField.getText();
            String password = passField.getText();
            String confPassword = cPassField.getText();

            ValidationResult usernameCheck = Validator.username(username);
            ValidationResult passwordCheck = Validator.password(password);
            ValidationResult nameCheck = (Validator.general(name));
            if (!usernameCheck.isValid() || !passwordCheck.isValid() || !nameCheck.isValid()) {
                if (!usernameCheck.isValid()) {
                    errorLbl.setText(usernameCheck.getReason());
                } else if (!passwordCheck.isValid()) {
                    errorLbl.setText(passwordCheck.getReason());
                } else {
                    errorLbl.setText(nameCheck.getReason());
                }
            } else {
                if (!password.equals(confPassword)) {
                    errorLbl.setText("Passwords don't match try again!");
                    return;
                }
                ValidationResult accountCreation = createAccount(username, name, password);
                if (!accountCreation.isValid()) {
                    errorLbl.setText(accountCreation.getReason());
                    return;
                }
                successPopup("Sign Up Successful!", "You can now login!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ValidationResult createAccount(String username, String name, String password) throws SQLException, NoSuchAlgorithmException {
        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        Connection connection = DriverManager.getConnection(url);
        // Sets up SQL connection

        PreparedStatement getStatement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE LOWER(username) = LOWER(?)");
        getStatement.setString(1, username);
        ResultSet results = getStatement.executeQuery();
        // Performs query

        if (results.next() && results.getInt(1) != 0) {
            return new ValidationResult(false, "Sorry, username already taken!");
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
        connection.close();
        return new ValidationResult(true);
    }
}


