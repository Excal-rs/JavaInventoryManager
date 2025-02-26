package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.Inventory;
import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.Classes.ValidationResult;
import com.inventorymanagementsystem.nea.ims.Classes.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditItemFormController extends DefaultController implements Initializable {
    @FXML
    private Spinner<Integer> quantSpinner;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Spinner<Double> priceSpinner;
    @FXML
    private ToggleButton cstmFieldToggle;
    @FXML
    private ToggleButton instanceToggle;
    @FXML
    private DatePicker purchaseDateSelector;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Label errorLbl;

    private Item item;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory.IntegerSpinnerValueFactory quantValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, 1, 1);
        quantSpinner.setValueFactory(quantValueFactory);
        // Initialises the quant min, max and initial value

        SpinnerValueFactory.DoubleSpinnerValueFactory priceValueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Integer.MAX_VALUE, 0.00, 1);
        priceSpinner.setValueFactory(priceValueFactory);
        // Initialises the price min, max and initial value

        TextField quantField = quantSpinner.getEditor();
        quantField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateIntSpinners(quantField, quantValueFactory.getMin(), quantValueFactory.getMax());
            }
        }));
        quantField.setOnAction(event -> {
            validateIntSpinners(quantField, quantValueFactory.getMin(), quantValueFactory.getMax());
            event.consume();
        });
        // Validate Inputs for quantity spinner

        TextField priceField = priceSpinner.getEditor();
        priceField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateDoubleSpinners(priceField, priceValueFactory.getMin(), priceValueFactory.getMax());
            }
        }));
        priceField.setOnAction(event -> {
            validateDoubleSpinners(priceField, priceValueFactory.getMin(), priceValueFactory.getMax());
            event.consume();
        });
        // Validate Inputs for price spinner
    }

    public void setItem(Item item) {
        this.item = item;
        nameField.setText(item.getName());
        descriptionArea.setText(item.getDescription());
        instanceToggle.setSelected(item.isTrackInstances());
        cstmFieldToggle.setSelected(item.isCustomFields());
        quantSpinner.getEditor().setText(Integer.toString(item.getQuantity()));
        priceSpinner.getEditor().setText(Double.toString(item.getPurchasePrice()));
        purchaseDateSelector.setValue(item.getDate());

        if (item.isTrackInstances()) {
            int quantity = item.getQuantity();
            SpinnerValueFactory.IntegerSpinnerValueFactory quantValueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) quantSpinner.getValueFactory();
            quantValueFactory.setMin(quantity);
        }
        // Updates the minimum value to be the current quantity, this is so that instances which may contain important information are not deleted.
    } // This will be used when the edit item form is opened, this is so that an item can be passed to it and prepopulate the form.


    // Button Actions --------------------------------------------------------------------------------------------------
    public void submit(ActionEvent event) {
        String description = descriptionArea.getText();
        double price = priceSpinner.getValue();
        LocalDate date = purchaseDateSelector.getValue();
        boolean customFields = cstmFieldToggle.isSelected();
        boolean trackInstances = instanceToggle.isSelected();
        int quantity = quantSpinner.getValue();

        ValidationResult descriptionResult = Validator.general(description);
        ValidationResult dateResult = Validator.date(date);

        if (!descriptionResult.isValid()) {
            errorLbl.setText(descriptionResult.getReason());
            return;
        } else if (!dateResult.isValid()) {
            errorLbl.setText(dateResult.getReason());
            return;
        }

        Item editedItem = item.copyItem();
        editedItem.setDescription(description);
        editedItem.setPurchasePrice(price);
        editedItem.setPurchaseDate(date);
        editedItem.setTrackInstances(trackInstances);
        editedItem.setCustomFields(customFields);
        editedItem.setQuantity(quantity);

        if (Inventory.editItem(editedItem).isValid()) {
            successPopup("Item successfully edited", "Item successfully edited");
        } else {
            errorLbl.setText("Item does not exist!");
        }
    }

    public void closeForm(ActionEvent event) throws IOException {
        boolean confirmed = confirmationDialogue("Close Form", "Are you sure you want to do this, any unsaved work will be lost?");
        if (confirmed) {
            Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            stage.close();
        }
    }

    public void deleteItem(ActionEvent event) {
        boolean confirmed = confirmationDialogue("Delete item", "This will permanently remove the item from the inventory.");
        if (confirmed) {
            Inventory.removeItem(item);
            successPopup("Item Removed", "Item Successfully removed from inventory!");
            Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            stage.close();
        }
    }


}
