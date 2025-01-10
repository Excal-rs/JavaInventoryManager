module com.inventorymanagementsystem.nea.ims {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.inventorymanagementsystem.nea.ims to javafx.fxml;
    exports com.inventorymanagementsystem.nea.ims;
}