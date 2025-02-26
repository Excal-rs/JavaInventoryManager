package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.Inventory;
import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.Classes.ItemInstance;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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
        fillItemsTable();
    }


    private void fillItemsTable() {
        HashMap<String, Item> items = Inventory.getItems(); // Fetch items from SQL database

        ObservableList<Item> itemList = FXCollections.observableArrayList(items.values());
        // Convert HashMap values into an ObservableList
        // Ensure the TableView has columns before adding data
        if (itemsTable.getColumns().isEmpty()) {
            TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

            TableColumn<Item, String> descriptionColumn = new TableColumn<>("Description");
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));


            TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));


            TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("purchasePrice"));


            TableColumn<Item, LocalDate> dateColumn = new TableColumn<>("Purchase Date");
            dateColumn.setCellValueFactory(new PropertyValueFactory<Item, LocalDate>("purchaseDate"));


            TableColumn<Item, Boolean> trackInstancesColumn = new TableColumn<>("Track Instances");
            trackInstancesColumn.setCellValueFactory(new PropertyValueFactory<Item, Boolean>("trackInstances"));


            TableColumn<Item, Boolean> customFieldsColumn = new TableColumn<>("Custom Fields");
            customFieldsColumn.setCellValueFactory(new PropertyValueFactory<Item, Boolean>("customFields"));

            itemsTable.getColumns().addAll(nameColumn, descriptionColumn, quantityColumn, priceColumn, dateColumn, trackInstancesColumn, customFieldsColumn);
        }

        // Set items in the TableView
        itemsTable.getItems().addAll(itemList);
    }



}
