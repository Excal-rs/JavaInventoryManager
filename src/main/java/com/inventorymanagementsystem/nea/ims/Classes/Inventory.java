package com.inventorymanagementsystem.nea.ims.Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    private static final String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
    private static final HashMap<String, Item> items = new HashMap<>();
    private static ArrayList<String> customFields = new ArrayList<>();
    static {
        updateItems();
        updateCustomFields();
    }


    // Item Management -------------------------------------------------------------------------------------------------
    public static void updateItems() {
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement getItemsStatement = connection.prepareStatement("SELECT * FROM items " +
                    "WHERE LOWER(userID) = LOWER(?);");
            getItemsStatement.setString(1, User.getUsername());
            ResultSet results = getItemsStatement.executeQuery();
            // Fetches result set from database

            while (results.next()) {
                Item newItem = new Item(results.getString("itemID"), results.getString("itemDescription"), results.getBoolean("trackInstances"),
                        results.getBoolean("useCustomFields"), results.getInt("purchasePrice"), results.getInt("purchaseDate"), results.getInt("quantity"));
                items.put(results.getString("itemID").toLowerCase(), newItem);
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateCustomFields() {
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement getCustomFieldsStatement = connection.prepareStatement("SELECT * FROM customFields " +
                    "WHERE LOWER(userID) = LOWER(?);");
            getCustomFieldsStatement.setString(1, User.getUsername());
            ResultSet results = getCustomFieldsStatement.executeQuery();
            // Fetches result set from database

            while (results.next()) {
                customFields.add(results.getString("fieldName"));
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ValidationResult addItem(Item item) {
        if (items.get(item.getName().toLowerCase()) != null) {
            return new ValidationResult(false, "Item already exists!");
        }
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO items " +
                    "VALUES (?,?,?,?,?,?,?,?);");
            insertStatement.setString(1, User.getUsername());
            insertStatement.setString(2, item.getName());
            insertStatement.setString(3, item.getDescription());
            insertStatement.setInt(4, item.getQuantity());
            insertStatement.setInt(5, (int) item.getPurchasePrice() * 100);
            insertStatement.setLong(6, Item.dateToUnix(item.getPurchaseDate()));
            insertStatement.setBoolean(7, item.isTrackInstances());
            insertStatement.setBoolean(8, item.isCustomFields());

            insertStatement.executeUpdate();

            if (item.isTrackInstances()) {
                PreparedStatement addAllInstances = connection.prepareStatement("INSERT INTO itemInstances VALUES (?,?,?,'N/A','N/A');");
                addAllInstances.setString(1, User.getUsername());
                addAllInstances.setString(2, item.getName());

                for (int i = 1; i < item.getQuantity() + 1; i++) {
                    addAllInstances.setInt(3, i);
                    addAllInstances.executeUpdate();
                    item.getInstances().put(i,new ItemInstance(item, i, "N/A", "N/A"));
                }
            }

            connection.close();
            items.put(item.getName().toLowerCase(), item.copyItem());
            return new ValidationResult(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ValidationResult removeItem(Item item) {
        if (items.get(item.getName().toLowerCase()) == null) {
            return new ValidationResult(false, "Item does not exist!");
        }
        try {
            Connection connection = DriverManager.getConnection(url);
            if (item.isTrackInstances()) {
                PreparedStatement removeInstancesStatement = connection.prepareStatement("DELETE FROM itemInstances WHERE LOWER(userID) = LOWER(?) AND LOWER(itemID) = LOWER(?);");
                removeInstancesStatement.setString(1, User.getUsername());
                removeInstancesStatement.setString(2, item.getName());
                removeInstancesStatement.executeUpdate();
            } // Removes all instances of the item if there are any.
            PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM items WHERE LOWER(userID) = LOWER(?) AND LOWER(itemID) = LOWER(?);");
            removeStatement.setString(1, User.getUsername());
            removeStatement.setString(2, item.getName());
            removeStatement.executeUpdate();
            // Remove Item associated with user
            connection.close();

            items.remove(item.getName().toLowerCase());
            // Remove locally

            return new ValidationResult(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ValidationResult editItem(Item item) {
        if (items.get(item.getName().toLowerCase()) == null) {
            return new ValidationResult(false, "Item does not exist");
        }
        try {
            Connection connection = DriverManager.getConnection(url);
            Item oldItem = items.get(item.getName().toLowerCase());

            if (oldItem.isTrackInstances() ^ item.isTrackInstances()) {
                if (!item.isTrackInstances()) {
                    PreparedStatement deleteInstancesStatement = connection.prepareStatement("DELETE FROM itemInstances " +
                            "WHERE LOWER(userID) = LOWER(?) AND LOWER(itemID) = LOWER(?);");
                    deleteInstancesStatement.setString(1, User.getUsername());
                    deleteInstancesStatement.setString(2, item.getName());
                    deleteInstancesStatement.executeUpdate();
                    // Remove all existing instances from the itemInstance table.
                } else {
                    PreparedStatement generateInstancesStatement = connection.prepareStatement("INSERT INTO itemInstances (userID, itemID, instanceID, notes, location) " +
                            "VALUES (?,?,?, 'N/A', 'N/A');");
                    generateInstancesStatement.setString(1, User.getUsername());
                    generateInstancesStatement.setString(2, item.getName());

                    for (int i = 1; i < item.getQuantity() + 1; i++) {
                        generateInstancesStatement.setInt(3, i);
                        generateInstancesStatement.executeUpdate();
                    }
                }
            }
            //  Handle change of instance value by either removing all instances or generating instances.

            if (item.isTrackInstances() && oldItem.isTrackInstances() && oldItem.getQuantity() < item.getQuantity()) {
                PreparedStatement generateInstancesStatement = connection.prepareStatement("INSERT INTO itemInstances (userID, itemID, instanceID, notes, location) " +
                        "VALUES (?,?,?,'N/A','N/A');");
                generateInstancesStatement.setString(1, User.getUsername());
                generateInstancesStatement.setString(2, item.getName());
                int firstIdentifier = item.generateInstanceId();
                for (int i = 0; i < item.getQuantity() - oldItem.getQuantity(); i++) {
                    generateInstancesStatement.setInt(3, i + firstIdentifier);
                    generateInstancesStatement.executeUpdate();
                }
            } // Adds new instances if quantity has changed and item had instances previously


            // TODO: Implement Later when custom fields are properly implemented

            PreparedStatement editStatement = connection.prepareStatement("UPDATE items " +
                    "SET itemDescription = ?, " +
                    "quantity = ?, " +
                    "purchasePrice = ?, " +
                    "purchaseDate = ?, " +
                    "trackInstances = ?, " +
                    "useCustomFields = ? " +
                    "WHERE LOWER(userID) = LOWER(?) AND LOWER(itemID) = LOWER(?);");
            editStatement.setString(1, item.getDescription());
            editStatement.setInt(2, item.getQuantity());
            editStatement.setInt(3, (int) item.getPurchasePrice() * 100);
            editStatement.setLong(4, Item.dateToUnix(item.getPurchaseDate()));
            editStatement.setBoolean(5, item.isTrackInstances());
            editStatement.setBoolean(6, item.isCustomFields());
            editStatement.setString(7, User.getUsername());
            editStatement.setString(8, item.getName());

            editStatement.executeUpdate();
            connection.close();
            item.updateInstances();
            items.put(item.getName().toLowerCase(), item.copyItem());
            return new ValidationResult(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Report generation -----------------------------------------------------------------------------------------------
    public static double getTotalInventoryValue() {
        ObservableList<Item> itemList = FXCollections.observableArrayList(items.values());
        double sum = 0;
        for (Item item : itemList) {
            sum += item.getPurchasePrice() * item.getQuantity();
        }
        return sum;
    }

    public static int getInventoryQuantity() {
        ObservableList<Item> itemList = FXCollections.observableArrayList(items.values());
        int sum = 0;
        for (Item item : itemList) {
            sum += item.getQuantity();
        }
        return sum;
    }

    // Getters ---------------------------------------------------------------------------------------------------------
    public static HashMap<String, Item> getItems() {
        return items;
    }

    public static String getUrl() {
        return url;
    }

    public static ArrayList<String> getCustomFields() {
        return customFields;
    }
}

