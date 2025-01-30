package com.inventorymanagementsystem.nea.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;


public class DefaultController {
    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;

    public void switchToScene(ActionEvent event, String filename) throws IOException {
        loader = new FXMLLoader(getClass().getResource(filename));
        // Gets the resource given
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load());
        // Loads the FXML file into the scene
        stage.setScene(scene);
        stage.show();
        // Scene switched
    }

    private void successPopup(String title, String description){
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle(title);
        popup.setContentText(description);
        // Creates the popup with the corresponding title and description

        popup.showAndWait();
        // Shows the popup to user and wait until it is closed.
    }
}
