package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
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
    @FXML
    private VBox cstmFieldSection;
    @FXML
    private ComboBox<String> cstmFieldTitle1;
    @FXML
    private TextField cstmField1;
    @FXML
    private ComboBox<String> cstmFieldTitle2;
    @FXML
    private TextField cstmField2;

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
        String description = descriptionArea.getText().isEmpty() ? "N/A" : descriptionArea.getText();
        int quantity = quantSpinner.getValue();
        double price = priceSpinner.getValue();
        LocalDate date = purchaseDateSelector.getValue() == null ? LocalDate.now() : purchaseDateSelector.getValue();
        boolean customFields = cstmFieldToggle.isSelected();
        boolean trackInstances = instanceToggle.isSelected();

        ValidationResult nameCheck = Validator.name(name);
        ValidationResult descriptionCheck = Validator.general(description);
        ValidationResult dateCheck = Validator.date(date);

        if (!nameCheck.isValid()) {
            errorLbl.setText(nameCheck.getReason());
            return;
        }
        if (!descriptionCheck.isValid()) {
            errorLbl.setText(descriptionCheck.getReason());
            return;
        }
        if (!dateCheck.isValid()) {
            errorLbl.setText(dateCheck.getReason());
            return;
        }

        CustomFieldValue[] customFieldValues = new CustomFieldValue[2];
        if (customFields){
            String cstmTitle1 = cstmField1.getText().isEmpty() ? null : cstmFieldTitle1.getValue();
            String cstmTitle2 = cstmField2.getText().isEmpty() ? null : cstmFieldTitle2.getValue();
            String cstmFieldValue1 = cstmTitle1 == null ? null : cstmField1.getText();
            String cstmFieldValue2 = cstmTitle2 == null ? null : cstmField2.getText();
            // Fetches inputted values

            ValidationResult title1Check = cstmTitle1 == null ? null : Validator.name(cstmTitle1);
            ValidationResult title2Check = cstmTitle2 == null ? null: Validator.name(cstmTitle2);
            ValidationResult value1Check = cstmFieldValue1 == null ? null : Validator.general(cstmFieldValue1);
            ValidationResult value2Check = cstmFieldValue2 == null ? null : Validator.general(cstmFieldValue2);
            // Validates inputs

            if (cstmTitle1 != null && !title1Check.isValid()){
                errorLbl.setText("Custom field title 1 can not be named that!");
                return;
            } if (cstmFieldValue1 != null && !value1Check.isValid()){
                errorLbl.setText("Custom field value 1 can not have that!");
                return;
            } if (cstmTitle2 != null && !title2Check.isValid()){
                errorLbl.setText("Custom field title 2 can not be named that!");
                return;
            } if (cstmFieldValue2 != null && !value2Check.isValid()) {
                errorLbl.setText("Custom field value 2 can not have that!");
                return;
            }

            customFieldValues[0] = cstmTitle1 == null ? null : new CustomFieldValue(cstmTitle1, cstmFieldValue1);
            customFieldValues[1] = cstmTitle2 == null ? null : new CustomFieldValue(cstmTitle2, cstmFieldValue2);
            // Sets the values of the arrays
        }

        Item newItem = new Item(name, description, trackInstances, customFields, price, date, quantity, customFieldValues);
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

    public void toggleTrackInstances(ActionEvent event) {
        if (instanceToggle.isSelected()) {
            instanceToggle.setText("On");
        } else {
            instanceToggle.setText("Off");
        }
    }

    public void toggleCustomFields(ActionEvent event) { // Changes the visibility of the custom fields section based on the toggle
        if (cstmFieldToggle.isSelected()) {
            cstmFieldToggle.setText("On");
            cstmFieldSection.setVisible(true);

            ArrayList<String> customFields = Inventory.getCustomFields();
            cstmFieldTitle1.getItems().setAll(customFields);
            // Populates the custom field title dropdown with the custom fields that already exist

            cstmField1.setDisable(true);
            cstmFieldTitle2.setDisable(true);
            cstmField2.setDisable(true);
            // Prevents the user from entering custom field values before selecting a title or adding info

            cstmFieldTitle1.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()){
                    cstmField1.setText("");
                    cstmField2.setDisable(true);
                }
                else { cstmField1.setDisable(false); }
            }); // Enables the first custom field value when the first custom field title is selected

            cstmField1.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    ArrayList<String> remainingFields = new ArrayList<>(customFields);
                    remainingFields.remove(cstmFieldTitle1.getValue());
                    cstmFieldTitle2.getItems().setAll(remainingFields);
                    cstmFieldTitle2.setDisable(false);
                } else {
                    cstmFieldTitle2.setValue("");
                    cstmFieldTitle2.setDisable(true);
                }
            }); // Enables the second custom field title dropdown when the first custom field value is entered

            cstmFieldTitle2.valueProperty().addListener((observable, oldValue, newValue) -> {
                cstmField2.setDisable(newValue.isEmpty());
                cstmField2.setText("");
                if (newValue.equals(cstmFieldTitle1.getValue())) {
                    errorLbl.setText("You cannot use duplicate fields for custom field titles.");
                    cstmFieldTitle2.setValue("");
                }
            }); // Enables the second custom field value when the second custom field title is selected

        } else {
            cstmFieldToggle.setText("Off");
            cstmFieldTitle1.setValue(null);
            cstmFieldTitle2.setValue(null);
            cstmField1.setText("");
            cstmField2.setText("");
            cstmFieldSection.setVisible(false);
        }
    }
}
