package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.Classes.ItemInstance;
import com.inventorymanagementsystem.nea.ims.Classes.ValidationResult;
import com.inventorymanagementsystem.nea.ims.Classes.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewInstanceController extends DefaultController implements Submittable {
    private Item item;
    @FXML
    private Label titleLbl;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea notesArea;
    @FXML
    private TextField locationField;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorLbl;

    public void setItem(Item item) {
        this.item = item;
        titleLbl.setText("Add New Instance - " + item.getName());
        nameField.setText(Integer.toString(item.generateInstanceId()));
    }

    @Override
    public void submit(ActionEvent event) {
        String instanceID = nameField.getText();
        String notes = notesArea.getText();
        String location = locationField.getText();

        ValidationResult notesCheck = Validator.general(notes);
        ValidationResult locationCheck = Validator.general(location);

        if (!notesCheck.isValid()) {
            errorLbl.setText(notesCheck.getReason());
            return;
        }
        if (!locationCheck.isValid()) {
            errorLbl.setText(locationCheck.getReason());
            return;
        } // Validate inputs

        ItemInstance newInstance = new ItemInstance(item, Integer.parseInt(instanceID), notes, location);
        if (item.addInstance(newInstance).isValid()) { // Add instance to item
            successPopup("Instance Added", "Instance has been successfully added to the item");
            closeForm(event);
        } else {
            errorLbl.setText("Error adding instance to item");
        }
    }

    public void cancel(ActionEvent event) {
        closeForm(event);
    }
}
