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

public class NewItemFormController extends DefaultController implements Initializable {
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
        SpinnerValueFactory<Integer> quantValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, 1, 1);
        quantSpinner.setValueFactory(quantValueFactory);
        // Initialises the quant min, max and initial value

        SpinnerValueFactory<Double> priceValueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999999, 0, 1);
        priceSpinner.setValueFactory(priceValueFactory);
        // Initialises the price min, max and initial value
    }

    // Button Actions --------------------------------------------------------------------------------------------------
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
        } else if (!descriptionResult.isValid()) {
            errorLbl.setText(descriptionResult.getReason());
        } else if (!dateResult.isValid()) {
            errorLbl.setText(dateResult.getReason());
        }

        Item newItem = new Item(name, description, trackInstances, customFields, price, date, quantity);
        if (Inventory.addItem(newItem).isValid()){
            successPopup("Item successfully added", "Item successfully added");
        } else {
            errorLbl.setText("Item already exists! Pick another name.");
        }
    }

    public void closeForm(ActionEvent event) throws IOException {
        boolean confirmed = confirmationDialogue("Close Form", "Are you sure you want to do this, any unsaved work will be lost?");
        if (confirmed) {
            Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            stage.close();
        }
    }


}
