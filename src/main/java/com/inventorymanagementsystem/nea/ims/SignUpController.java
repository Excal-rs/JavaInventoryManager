package com.inventorymanagementsystem.nea.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;


public class SignUpController extends DefaultController {
    @FXML
    private Label errorLbl;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button signupBtn;
    @FXML
    private Button loginBtn;

    private void login(ActionEvent event){
        try {
            switchToScene(event, "login.fxml");
        } catch (IOException e) {
            System.out.println("Change scene caused error");
            // TODO: when errorPopup is created, add one here
        }
    }

}
