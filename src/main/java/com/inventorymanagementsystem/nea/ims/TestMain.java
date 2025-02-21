package com.inventorymanagementsystem.nea.ims;

import com.inventorymanagementsystem.nea.ims.Classes.Inventory;
import com.inventorymanagementsystem.nea.ims.Classes.Item;
import com.inventorymanagementsystem.nea.ims.Classes.User;

import java.sql.SQLException;

public class TestMain {
    public static void main(String[] args) throws SQLException {
        User.setCurrentUser("saifali_razaq");
        Item TestItem = new Item("TestItem", "None", true, false, 10000, 1642204800, 5);

//        System.out.println("Performing test for addItem...");
//        Inventory.addItem(TestItem);

        System.out.println("Performing test for editItem...");
        TestItem.setTrackInstances(false);
        Inventory.editItem(TestItem);

//        System.out.println("Performing Test for updateItems...");
//        HashMap<String, Item> items = Inventory.getItems();
//        System.out.println(items);

//        System.out.println("Performing Test for removeItems...");
//        Inventory.removeItem(TestItem);
    }
}
