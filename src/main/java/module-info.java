module com.inventorymanagementsystem.nea.ims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    requires jdk.compiler;


    opens com.inventorymanagementsystem.nea.ims to javafx.fxml;
    exports com.inventorymanagementsystem.nea.ims;
    exports com.inventorymanagementsystem.nea.ims.Controllers;
    opens com.inventorymanagementsystem.nea.ims.Controllers to javafx.fxml;
    exports com.inventorymanagementsystem.nea.ims.Classes;
    opens com.inventorymanagementsystem.nea.ims.Classes to javafx.fxml;
}