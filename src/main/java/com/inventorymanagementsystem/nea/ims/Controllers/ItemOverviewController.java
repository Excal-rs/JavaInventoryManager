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

public class ItemOverviewController extends DefaultController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialiseTable();
        setUpContextMenu();
        // Sets up table

        totalValueLbl.setText(String.format("£%.2f", item.getTotalValue()));
        quantLbl.setText(Integer.toString(item.getQuantity()));
    }



    // Table Related Methods -----------------------------------------------------------------------------------------------------
    private void initialiseTable() {
        HashMap<Integer, ItemInstance> instances = item.getInstances();

        ObservableList<ItemInstance> instancesList = FXCollections.observableArrayList(item.getInstances().values());
        // Convert HashMap values into an ObservableList
        // Ensure the TableView has columns before adding data
        if (instanceTable.getColumns().isEmpty()) {
            TableColumn<ItemInstance, Integer> identifierColumn = new TableColumn<>("Identifier");
            identifierColumn.setCellValueFactory(new PropertyValueFactory<>("instanceID"));

            TableColumn<ItemInstance, String> notesColumn = new TableColumn<>("Notes");
            notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

            TableColumn<ItemInstance, String> locationColumn = new TableColumn<>("Location");
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));


            instanceTable.getColumns().addAll(identifierColumn, notesColumn, locationColumn);
        }

        // Set items in the TableView
        instanceTable.getItems().addAll(instancesList);
    }

    private void setUpContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit Instance");
        editItem.setOnAction(e -> {
            ItemInstance selectedInstance = instanceTable.getSelectionModel().getSelectedItem();
            // Gets the selected item
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FXML/editInstanceForm.fxml"));
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(MainApplication.class.getResource("styles/inventoryForms.css").toExternalForm());
                // Loads FXML and CSS for scene

                EditInstanceController controller = loader.getController();
                controller.setInstance(selectedInstance);
                // Passes the selected item to the edit item form so that it can be edited

                Stage stage = new Stage();
                stage.setTitle("IMS - Edit Instance");
                stage.setWidth(1000);
                stage.setHeight(700);
                // Adds title and size of stage

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(instanceTable.getScene().getWindow());
                // Makes window a modal type so cannot interact with dashboard while it is open

                stage.setScene(scene);
                stage.showAndWait();
                refreshInfo();
                // Creates new window
            } catch (Exception exception) {
                throw new RuntimeException();
            }
        }); // Creates an entry in context menu to edit item

        MenuItem deleteItem = new MenuItem("Delete Instance");
        deleteItem.setOnAction(e -> {
            ItemInstance selectedInstance = instanceTable.getSelectionModel().getSelectedItem();
            boolean confirmed = confirmationDialogue("Delete Item Instance", "Are you sure you want to delete this instance?");
            if (confirmed) {
                item.removeInstance(selectedInstance);
                refreshInfo();
                successPopup("Instance Removed", "Instance successfully removed from inventory!");
                // Removes instance from inventory
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        instanceTable.setContextMenu(contextMenu);
        // Adds the context menu to the table
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
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FXML/addInstanceForm.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("styles/inventoryForms.css").toExternalForm());
            // Loads FXML file and adds CSS file

            NewInstanceController controller = new NewInstanceController();
            controller.setItem(item);
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
        if (confirmed){
            switchToScene(event, "dashboard.fxml", new String[] {"dashboard.css"}, "IMS - Dashboard");
        }
    }



}

