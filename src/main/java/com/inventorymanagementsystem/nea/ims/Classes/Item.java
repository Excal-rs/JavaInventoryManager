package com.inventorymanagementsystem.nea.ims.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class Item {
    private final String name;
    private String description;
    private boolean trackInstances;
    private boolean customFields;
    private double purchasePrice;
    private LocalDate purchaseDate;
    private int quantity;
    private HashMap<Integer, ItemInstance> instances;
    private CustomFieldValue[] customFieldValues;

    // Constructors ----------------------------------------------------------------------------------------------------
    public Item(String name, String description, boolean trackInstances,
                boolean customFields, int purchasePrice, long purchaseDate, int quantity, CustomFieldValue[] customFieldValues) {
        this.name = name;
        this.description = description;
        this.trackInstances = trackInstances;
        this.customFields = customFields;
        this.purchasePrice = (double) purchasePrice / 100.0;
        this.purchaseDate = unixToDate(purchaseDate);
        // Converts unix time to a formatted string
        this.quantity = quantity;
        this.customFieldValues = customFieldValues;

        updateInstances();
    }

    public Item(String name, String description, boolean trackInstances, boolean customFields, double purchasePrice, LocalDate purchaseDate, int quantity, CustomFieldValue[] customFieldValues) {
        this.name = name;
        this.description = description;
        this.trackInstances = trackInstances;
        this.customFields = customFields;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.customFieldValues = customFieldValues;

        updateInstances();
    }


    // Used when copying items;
    public Item(Item item) {
        this.name = item.getName();
        this.description = item.getDescription();
        this.trackInstances = item.isTrackInstances();
        this.customFields = item.isCustomFields();
        this.purchasePrice = item.getPurchasePrice();
        this.purchaseDate = item.getPurchaseDate();
        this.quantity = item.getQuantity();
        this.instances = item.getInstances();
        this.customFieldValues = item.customFieldValues;
    }


    // Misc ------------------------------------------------------------------------------------------------------------
    public static long dateToUnix(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    private static LocalDate unixToDate(long unixTime) {
        return Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Item copyItem() {
        return new Item(this);
    }

    // Instance Management ---------------------------------------------------------------------------------------------
    public void updateInstances() {
        if (!trackInstances) {
            instances = null;
            // Check if track instances is true
        }
        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        try (Connection connection = DriverManager.getConnection(url)){
            // Sets up SQL connection

            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM itemInstances " +
                    "WHERE LOWER(userID) = LOWER(?) " +
                    "AND LOWER(itemID) = LOWER(?);");
            getStatement.setString(1, User.getUsername());
            getStatement.setString(2, this.name);

            ResultSet results = getStatement.executeQuery();

            instances = instances == null ? new HashMap<>() : instances;
            // Initialise if instances is null, otherwise do nothing

            while (results.next()) {
                ItemInstance newInstance = new ItemInstance(this, results.getInt("instanceID"), results.getString("notes"), results.getString("location"));
                instances.put(results.getInt("instanceID"), newInstance);
            } // Copies results of query into a hashmap of instances so it can stay even after connection is closed

            // Performs query and returns results
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ValidationResult addInstance(ItemInstance instance) {
        if (!trackInstances) {
            return new ValidationResult(false, "Item set to not track instances!");
            // Check if track instances is true
        }
        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        try (Connection connection = DriverManager.getConnection(url)){
            // Sets up SQL connection

            PreparedStatement getStatement = connection.prepareStatement("SELECT COUNT(*) FROM itemInstances WHERE LOWER(userID) = LOWER(?) " +
                    "AND LOWER(itemID) = LOWER(?) AND instanceID = ?;");
            getStatement.setString(1, User.getUsername());
            getStatement.setString(2, this.name);
            getStatement.setInt(3, instance.getIdentifier());
            ResultSet results = getStatement.executeQuery();
            if (results.next() && results.getInt(1) != 0) {
                return new ValidationResult(false, "Instance already exists!");
            }

            PreparedStatement updateStatement = connection.prepareStatement("INSERT INTO itemInstances (userID, itemID, instanceID, notes, location) " +
                    "VALUES (?,?,?,?,?);");
            updateStatement.setString(1, User.getUsername());
            updateStatement.setString(2, this.name);
            updateStatement.setInt(3, instance.getIdentifier());
            updateStatement.setString(4, instance.getNotes());
            updateStatement.setString(5, instance.getLocation());
            // Sets values of prepared statement

            updateStatement.executeUpdate();
            connection.close();
            // Performs update

            quantity++;
            Inventory.editItem(this);
            instances.put(instance.getIdentifier(), instance);
            return new ValidationResult(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ValidationResult removeInstance(ItemInstance instance) {
        if (!trackInstances) {
            return new ValidationResult(false, "Item set to not track instances!");
        }
        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        try (Connection connection = DriverManager.getConnection(url)){
            // Sets up SQL connection

            PreparedStatement updateStatement = connection.prepareStatement("DELETE FROM itemInstances " +
                    "WHERE LOWER(userID) == LOWER(?) AND " +
                    "LOWER(itemID) == LOWER(?) AND " +
                    "instanceID == ?");

            updateStatement.setString(1, User.getUsername());
            updateStatement.setString(2, this.name);
            updateStatement.setInt(3, instance.getIdentifier());
            // Sets values of prepared statement

            updateStatement.executeUpdate();
            connection.close();
            // Performs update

            quantity--;
            Inventory.editItem(this);
            instances.remove(instance.getIdentifier());
            return new ValidationResult(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ValidationResult editInstance(ItemInstance instance) {
        if (!trackInstances) {
            return new ValidationResult(false, "Item set to not track instances!");
        }
        String url = "jdbc:sqlite:src/main/resources/com/inventorymanagementsystem/nea/ims/SQLdb/IMS_database";
        try (Connection connection = DriverManager.getConnection(url)){
            // Sets up SQL connection

            PreparedStatement updateStatement = connection.prepareStatement("UPDATE itemInstances " +
                    "SET notes = ?, location = ?" +
                    "WHERE LOWER(userID) = LOWER(?) AND LOWER(itemID) = LOWER(?) AND instanceID = ?");

            updateStatement.setString(1, instance.getNotes());
            updateStatement.setString(2, instance.getLocation());
            updateStatement.setString(3, User.getUsername());
            updateStatement.setString(4, this.name);
            updateStatement.setInt(5, instance.getIdentifier());
            // Sets values of prepared statement

            updateStatement.executeUpdate();
            connection.close();
            // Performs update

            instances.put(instance.getIdentifier(), instance);
            return new ValidationResult(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int generateInstanceId() {
        int largestKey = 0;
        if (instances.isEmpty()) {
            return largestKey + 1;
        }
        for (Map.Entry<Integer, ItemInstance> entry : instances.entrySet()) {
            if (entry.getKey() > largestKey) {
                largestKey = entry.getKey();
            }
        }
        // gets the current largest instance ID.
        return largestKey + 1;
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public HashMap<Integer, ItemInstance> getInstances() {
        return instances;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public double getTotalValue() {
        return quantity * purchasePrice;
    }

    public CustomFieldValue[] getCustomFieldValues() {
        return customFieldValues;
    }

    public boolean isTrackInstances() {
        return trackInstances;
    }

    public boolean isCustomFields() {
        return customFields;
    }



    // Setters ---------------------------------------------------------------------------------------------------------

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setIsTrackInstances(boolean trackInstances) {
        this.trackInstances = trackInstances;
    }

    public void setIsCustomFields(boolean customFields) {
        this.customFields = customFields;
    }

    public void setCustomFields(CustomFieldValue[] customFieldValues) {
        this.customFieldValues = customFieldValues;
    }
}
