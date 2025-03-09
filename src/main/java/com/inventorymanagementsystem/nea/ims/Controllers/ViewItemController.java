package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.CustomFieldValue;
import com.inventorymanagementsystem.nea.ims.Classes.Inventory;
import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ViewItemController extends DefaultController {
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Spinner<Integer> quantSpinner;
    @FXML
    private Spinner<Double> priceSpinner;
    @FXML
    private DatePicker purchaseDateSelector;
    @FXML
    private ToggleButton instanceToggle;
    @FXML
    private ToggleButton cstmFieldToggle;
    @FXML
    private VBox cstmFieldSection;
    @FXML
    private ComboBox<String> cstmFieldTitle1;
    @FXML
    private ComboBox<String> cstmFieldTitle2;
    @FXML
    private TextField cstmField1;
    @FXML
    private TextField cstmField2;
    @FXML
    private Button editBtn;
    @FXML
    private Button overviewBtn;
    @FXML
    private Button cancelBtn;

    private Item item;

    public void setItem(Item givenItem) {
        this.item = givenItem;
        nameField.setText(item.getName());
        descriptionArea.setText(item.getDescription());
        quantSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, item.getQuantity()));
        priceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, item.getPurchasePrice()));
        purchaseDateSelector.setValue(item.getPurchaseDate());
        instanceToggle.setSelected(item.isTrackInstances());
        cstmFieldToggle.setSelected(item.isCustomFields());
        // Prepopulates the fields with the item's data

        if (item.isCustomFields()){
            CustomFieldValue[] customFields = item.getCustomFieldValues();
            if (customFields[0] != null){
                cstmFieldSection.setVisible(true);
                cstmFieldTitle1.setValue(customFields[0].getTitle());
                cstmField1.setText(customFields[0].getValue());
                if (customFields[1] != null){
                    cstmFieldTitle2.setValue(customFields[1].getTitle());
                    cstmField2.setText(customFields[1].getValue());
                } else {
                    cstmFieldTitle2.setVisible(false);
                    cstmField2.setVisible(false);
                }
            }
        }
        if (!item.isTrackInstances()) {
            overviewBtn.setVisible(false);
        } else {
            instanceToggle.setText("On");
        }// Since overview is based on showing instances, it should only be visible if instances are tracked
    }

    public void switchToEditItem(ActionEvent event) {
        boolean confirmed = confirmationDialogue("Edit Item", "Are you sure you want to edit this item?");
        if (confirmed) {
            try {
                loader = new FXMLLoader(MainApplication.class.getResource("FXML/editItem.fxml"));
                // Gets the resource given
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(loader.load());
                scene.getStylesheets().add(MainApplication.class.getResource("styles/inventoryForms.css").toExternalForm());
                // Loads the FXML file and resources into the scene

                EditItemController controller = loader.getController();
                controller.setItem(Inventory.getItems().get(item.getName().toLowerCase()));
                // Assigns item

                stage.setTitle("IMS - Edit " + item.getName());
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
                // Scene switched and displayed
            } catch (Exception e) {
                throw new RuntimeException("Error switching to edit item scene", e);
            }
        }
    }

    public void switchToOverview(ActionEvent event) {
        try {
            // Switch to the item overview scene
            loader = new FXMLLoader(MainApplication.class.getResource("FXML/itemOverview.fxml"));
            // Gets the resource given
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("styles/dashboard.css").toExternalForm());
            // Loads the FXML file and resources into the scene

            ItemOverviewController controller = loader.getController();
            controller.setItem(Inventory.getItems().get(item.getName().toLowerCase()));
            // Assigns item

            stage.setTitle("IMS - " + item.getName() + " overview");
            stage.setWidth(1280);
            stage.setHeight(800);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Error switching to edit item scene", e);
        }
    }

    public void cancel(ActionEvent event) {
        closeForm(event);
    }


}
