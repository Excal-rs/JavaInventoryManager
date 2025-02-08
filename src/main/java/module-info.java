module com.inventorymanagementsystem.nea.ims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;


    opens com.inventorymanagementsystem.nea.ims to javafx.fxml;
    exports com.inventorymanagementsystem.nea.ims;
}