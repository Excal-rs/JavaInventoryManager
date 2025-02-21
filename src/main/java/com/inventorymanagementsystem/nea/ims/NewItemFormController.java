package com.inventorymanagementsystem.nea.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewItemFormController extends DefaultController implements Initializable {
    @FXML
    private Spinner<Integer> quantSpinner;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ToggleButton cstmFieldToggle;
    @FXML
    private ToggleButton instanceToggle;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorLbl;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999,1,1);
        quantSpinner.setValueFactory(valueFactory);
        // Initialises the Spinner min, max and initial value
    }

    public void closeForm(ActionEvent event) throws IOException {
        boolean confirmed = confirmationDialogue("Close Form", "Are you sure you want to do this, any unsaved work will be lost?");
        if (confirmed) {
//            String[] cssfiles = {};
//            switchToScene(event, "overview.fxml", cssfiles, "IMS - Overview");
             Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
             stage.close();
        }
    }


}
