package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


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

    if (!item.isTrackInstances()) {
        overviewBtn.setVisible(true);
    } // Since overview is based on showing instances, it should only be visible if instances are tracked
}

    public void switchToEditItem(ActionEvent event) {
        boolean confirmed = confirmationDialogue("Edit Item", "Are you sure you want to edit this item?");
        if (confirmed) {
            try {
                // Switch to the edit item scene
                switchToScene(event, "editItem.fxml", new String[]{"inventoryForms.css"}, String.format("IMS - Edit %s", item.getName()));
                EditItemController controller = new EditItemController();
                controller.setItem(item); // Pass the current item to the edit item controller
            } catch (Exception e) {
                throw new RuntimeException("Error switching to edit item scene", e);
            }
        }
    }

    public void switchToOverview(ActionEvent event) {
        try {
            // Switch to the item overview scene
            switchToScene(event, "itemOverview.fxml", new String[]{"dashboard.css"}, String.format("IMS - %s Overview", item.getName()), 1280, 800);
            ItemOverviewController controller = new ItemOverviewController();
            controller.setItem(item); // Pass the current item to the item overview controller
        } catch (Exception e) {
            throw new RuntimeException("Error switching to edit item scene", e);
        }
    }

    public void cancel(ActionEvent event) {
        closeForm(event);
    }


}
