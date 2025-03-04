package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.Inventory;
import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.Classes.ItemInstance;
import com.inventorymanagementsystem.nea.ims.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ItemOverviewController extends DefaultController {
    @FXML
    private Button exitBtn;
    @FXML
    private Button addItemBtn;
    @FXML
    private Label titleLbl;
    @FXML
    private TableView<ItemInstance> instanceTable;
    @FXML
    private Button addInstanceBtn;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Label quantLbl;
    @FXML
    private Label totalValueLbl;

    private Item item;


    public void setItem(Item givenItem) {
        this.item = givenItem;
        totalValueLbl.setText(String.format("£%.2f", item.getTotalValue()));
        quantLbl.setText(Integer.toString(item.getQuantity()));

        initialiseTable();
    }

    // Table Related Methods -----------------------------------------------------------------------------------------------------
    private void initialiseTable() {
        HashMap<Integer, ItemInstance> instances = item.getInstances();

        ObservableList<ItemInstance> instancesList = FXCollections.observableArrayList(item.getInstances().values());
        // Convert HashMap values into an ObservableList
        // Ensure the TableView has columns before adding data
        if (instanceTable.getColumns().isEmpty()) {
            TableColumn<ItemInstance, Integer> identifierColumn = new TableColumn<>("Identifier");
            identifierColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));

            TableColumn<ItemInstance, String> notesColumn = new TableColumn<>("Notes");
            notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

            TableColumn<ItemInstance, String> locationColumn = new TableColumn<>("Location");
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));


            instanceTable.getColumns().addAll(identifierColumn, notesColumn, locationColumn);
        }

        // Set items in the TableView
        instanceTable.getItems().addAll(instancesList);
    }


    public void searchTable() {
        String search = searchField.getText().toLowerCase();
        ObservableList<ItemInstance> instances = FXCollections.observableArrayList(item.getInstances().values());
        // Gets search query and instances in table

        instanceTable.getItems().clear();
        // Clears the table of all items before updating it with the search query results

        for (ItemInstance instance : item.getInstances().values()) {
            if (Integer.toString(instance.getIdentifier()).contains(search) || instance.getNotes().toLowerCase().contains(search)) {
                instanceTable.getItems().add(instance);
            }
        }
        // Adds items to the table if they match the search query
    }

    public void refreshInfo() {
        totalValueLbl.setText(String.format("£%.2f", item.getTotalValue()));
        quantLbl.setText(Integer.toString(item.getQuantity()));

        searchTable();
        // Refreshes the table and report section
    }

    // Form Navigation -------------------------------------------------------------------------------------------------
    public void openAddInstanceScene() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FXML/addInstance.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("styles/inventoryForms.css").toExternalForm());
            // Loads FXML file and adds CSS file

            NewInstanceController controller = loader.getController();
            controller.setItem(Inventory.getItems().get(item.getName().toLowerCase()));
            // Passes item

            Stage stage = new Stage();
            stage.setTitle("IMS - Add New Instance");
            stage.setWidth(1000);
            stage.setHeight(700);
            // Sets title and size of stage

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(instanceTable.getScene().getWindow());
            // Makes window a modal type so cannot interact with dashboard while it is open

            stage.setScene(scene);
            stage.showAndWait();
            refreshInfo();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void logout(ActionEvent event) throws IOException {
        boolean confirmed = confirmationDialogue("Open Dashboard", "Are you sure you want to open the dashboard?");
        if (confirmed) {
            switchToScene(event, "dashboard.fxml", new String[]{"dashboard.css"}, "IMS - Dashboard");
        }
    }


    public void openAddItemScene(ActionEvent actionEvent) {
    }
}

