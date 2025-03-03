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

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewItemController extends DefaultController implements Initializable, Submittable {
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
    private Label errorLbl;

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
                Validator.intSpinners(quantField, quantValueFactory.getMin(), quantValueFactory.getMax());
            }
        }));
        quantField.setOnAction(event -> {
            Validator.intSpinners(quantField, quantValueFactory.getMin(), quantValueFactory.getMax());
            event.consume();
        });
        // Validate Inputs for quantity spinner

        TextField priceField = priceSpinner.getEditor();
        priceField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                Validator.doubleSpinners(priceField, (int) priceValueFactory.getMin(), (int) priceValueFactory.getMax());
            }
        }));
        priceField.setOnAction(event -> {
            Validator.doubleSpinners(priceField, (int) priceValueFactory.getMin(), (int) priceValueFactory.getMax());
            event.consume();
        });
        // Validate Inputs for price spinner

    }


    // Button Actions --------------------------------------------------------------------------------------------------
    @Override
    public void submit(ActionEvent event) {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        double price = priceSpinner.getValue();
        LocalDate date = purchaseDateSelector.getValue();
        boolean customFields = cstmFieldToggle.isSelected();
        boolean trackInstances = instanceToggle.isSelected();
        int quantity = quantSpinner.getValue();

        ValidationResult nameResult = Validator.name(name);
        ValidationResult descriptionResult = Validator.general(description);
        ValidationResult dateResult = Validator.date(date);

        if (!nameResult.isValid()) {
            errorLbl.setText(nameResult.getReason());
            return;
        } else if (!descriptionResult.isValid()) {
            errorLbl.setText(descriptionResult.getReason());
            return;
        } else if (!dateResult.isValid()) {
            errorLbl.setText(dateResult.getReason());
            return;
        }

        Item newItem = new Item(name, description, trackInstances, customFields, price, date, quantity);
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        if (Inventory.addItem(newItem).isValid()) {
            successPopup("Item successfully added", "Item successfully added");
            stage.close();
        } else {
            errorLbl.setText("Item already exists! Pick another name.");
        }
    }

    public void cancel(ActionEvent event) {
        closeForm(event);
    }

}
