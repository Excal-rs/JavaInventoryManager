package com.inventorymanagementsystem.nea.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;


public class DefaultController {
    public Stage stage;
    public Scene scene;
    public FXMLLoader loader;

    public void switchToScene(ActionEvent event, String filename, String[] cssFiles, String windowTitle) throws IOException {
        loader = new FXMLLoader(getClass().getResource(filename));
        // Gets the resource given
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load());
        if (cssFiles.length != 0) {
            for (String cssfile : cssFiles){
                scene.getStylesheets().add(getClass().getResource(cssfile).toExternalForm());
            }
        }
        // Loads the FXML file and resources into the scene

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.show();
        // Scene switched and displayed
    }

    public void switchToScene(ActionEvent event, String filename, String[] cssFiles, String windowTitle, int width, int height) throws IOException {
        loader = new FXMLLoader(getClass().getResource(filename));
        // Gets the resource given
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load());
        if (cssFiles.length != 0) {
            for (String cssfile : cssFiles){
                scene.getStylesheets().add(getClass().getResource(cssfile).toExternalForm());
            }
        }
        // Loads the FXML file and resources into the scene

        stage.setTitle(windowTitle);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setScene(scene);
        stage.show();
        // Scene switched and displayed
    }

    public void successPopup(String title, String description){
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle(title);
        popup.setContentText(description);
        // Creates the popup with the corresponding title and description

        popup.showAndWait();
        // Shows the popup to user and wait until it is closed.
    }
}
