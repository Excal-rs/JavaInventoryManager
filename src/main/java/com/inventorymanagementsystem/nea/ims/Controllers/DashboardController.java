package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.Inventory;
import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.Classes.User;
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

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController extends DefaultController implements Initializable {
    @FXML
    private TableView<Item> itemsTable;
    @FXML
    private Button addItemBtn;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Label totalQuantLbl;
    @FXML
    private Label totalPriceLbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialiseTable();
        setUpContextMenu();
        // Sets up table

        totalPriceLbl.setText(String.format("£%.2f", Inventory.getTotalInventoryValue()));
        totalQuantLbl.setText(Integer.toString(Inventory.getInventoryQuantity()));
    }

    // Table Related Methods -----------------------------------------------------------------------------------------------------
    private void initialiseTable() {
        HashMap<String, Item> items = Inventory.getItems(); // Fetch items from SQL database

        ObservableList<Item> itemList = FXCollections.observableArrayList(items.values());
        // Convert HashMap values into an ObservableList
        // Ensure the TableView has columns before adding data
        if (itemsTable.getColumns().isEmpty()) {
            TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Item, String> descriptionColumn = new TableColumn<>("Description");
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));


            TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));


            TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));


            TableColumn<Item, LocalDate> dateColumn = new TableColumn<>("Purchase Date");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));


            TableColumn<Item, Boolean> trackInstancesColumn = new TableColumn<>("Track Instances");
            trackInstancesColumn.setCellValueFactory(new PropertyValueFactory<>("trackInstances"));


            TableColumn<Item, Boolean> customFieldsColumn = new TableColumn<>("Custom Fields");
            customFieldsColumn.setCellValueFactory(new PropertyValueFactory<>("customFields"));

            itemsTable.getColumns().addAll(nameColumn, descriptionColumn, quantityColumn, priceColumn, dateColumn, trackInstancesColumn, customFieldsColumn);
        }

        // Set items in the TableView
        itemsTable.getItems().addAll(itemList);
    }

    private void setUpContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit Item");
        editItem.setOnAction(e -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            // Gets the selected item
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FXML/editItemForm.fxml"));
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(MainApplication.class.getResource("styles/inventoryForms.css").toExternalForm());
                // Loads FXML and CSS for scene

                EditItemController controller = loader.getController();
                controller.setItem(selectedItem);
                // Passes the selected item to the edit item form so that it can be edited

                Stage stage = new Stage();
                stage.setTitle("IMS - Edit Item");
                stage.setWidth(1000);
                stage.setHeight(700);
                // Adds title and size of stage

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(itemsTable.getScene().getWindow());
                // Makes window a modal type so cannot interact with dashboard while it is open

                stage.setScene(scene);
                stage.showAndWait();
                refreshInfo();
                // Creates new window
            } catch (Exception exception) {
                throw new RuntimeException();
            }
        }); // Creates an entry in context menu to edit item

        MenuItem deleteItem = new MenuItem("Delete Item");
        deleteItem.setOnAction(e -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            boolean confirmed = confirmationDialogue("Delete Item", "Are you sure you want to delete this item?");
            if (confirmed) {
                Inventory.removeItem(selectedItem);
                refreshInfo();
                successPopup("Item Removed", "Item successfully removed from inventory!");
                // Removes item from inventory
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        itemsTable.setContextMenu(contextMenu);
        // Adds the context menu to the table
    }

    public void searchTable() {
        String search = searchField.getText().toLowerCase();
        ObservableList<Item> items = FXCollections.observableArrayList(Inventory.getItems().values());
        // Gets search query and items in table

        itemsTable.getItems().clear();
        // Clears the table of all items before updating it with the search query results

        for (Item item : Inventory.getItems().values()) {
            if (item.getName().toLowerCase().contains(search)) {
                itemsTable.getItems().add(item);
            }
        }
        // Adds items to the table if they match the search query
    }

    public void refreshInfo() {
        totalPriceLbl.setText(String.format("£%.2f", Inventory.getTotalInventoryValue()));
        totalQuantLbl.setText(Integer.toString(Inventory.getInventoryQuantity()));

        searchTable();
        // Refreshes the table and report section
    }

    // Form Navigation -------------------------------------------------------------------------------------------------
    public void switchToAddItemScene() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FXML/addItemForm.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("styles/inventoryForms.css").toExternalForm());
            // Loads FXML file and adds CSS file

            Stage stage = new Stage();
            stage.setTitle("IMS - Add New Item");
            stage.setWidth(1000);
            stage.setHeight(700);
            // Sets title and size of stage

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(itemsTable.getScene().getWindow());
            // Makes window a modal type so cannot interact with dashboard while it is open

            stage.setScene(scene);
            stage.showAndWait();
            refreshInfo();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void logout(ActionEvent event) {
        try {
            boolean confirmed = confirmationDialogue("Log Out", "Are you sure you want to log out?");
            if (confirmed) {
                User.setName(null);
                User.setUsername(null); // Ensures user is logged out and clears user data
                switchToScene(event, "login.fxml", new String[]{"signupLogin.css"}, "IMS - Log In", 1000, 700);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}

