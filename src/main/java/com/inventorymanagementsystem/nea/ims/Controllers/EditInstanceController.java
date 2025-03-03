package com.inventorymanagementsystem.nea.ims.Controllers;

import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.Classes.ItemInstance;
import com.inventorymanagementsystem.nea.ims.Classes.ValidationResult;
import com.inventorymanagementsystem.nea.ims.Classes.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditInstanceController extends DefaultController implements Submittable{
    private ItemInstance instance;
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

    public void setInstance(ItemInstance itemInstance) {
        this.instance = itemInstance.copyInstance();
        this.item = instance.getItem();
        titleLbl.setText("Edit Instance - " + item.getName());
        nameField.setText(Integer.toString(instance.getIdentifier()));
        notesArea.setText(instance.getNotes());
        locationField.setText(instance.getLocation());
    }

    @Override
    public void submit(ActionEvent event) {
        String instanceID = nameField.getText();
        String notes = notesArea.getText();
        String location = locationField.getText();

        ValidationResult notesCheck = Validator.general(notes);
        ValidationResult locationCheck = Validator.general(location);

        if (!notesCheck.isValid()){
            errorLbl.setText(notesCheck.getReason());
            return;
        } if (!locationCheck.isValid()){
            errorLbl.setText(locationCheck.getReason());
            return;
        } // Validate inputs

        instance.setNotes(notes);
        instance.setLocation(location);
        if (item.editInstance(instance).isValid()){
            closeForm(event);
            successPopup("Instance Updated", "Instance has been successfully updated");
        } else {
            errorLbl.setText("Error updating instance");
        }
    }

    public void cancel(ActionEvent event) {
        closeForm(event);
    }

    public void delete(ActionEvent event) {
        boolean confirmed = confirmationDialogue("Delete Instance", "Are you sure you want to delete this instance?");
        if (confirmed){
            item.removeInstance(instance);
            successPopup("Instance Deleted", "Instance has been successfully deleted");
            Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            stage.close();
        }
    }
}
