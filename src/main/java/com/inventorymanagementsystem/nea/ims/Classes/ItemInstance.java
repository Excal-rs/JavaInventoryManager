package com.inventorymanagementsystem.nea.ims.Classes;

public class ItemInstance {
    private final Item item;
    private final int identifier;
    private String notes;
    private String location;

    public ItemInstance(Item item, int identifier, String notes, String location) {
        this.item = item;
        this.identifier = identifier;
        this.notes = notes;
        this.location = location;
    }

    public ItemInstance(ItemInstance instance) {
        this.item = instance.getItem();
        this.identifier = instance.getIdentifier();
        this.notes = instance.getNotes();
        this.location = instance.getLocation();
    } // User for cloning instances

    public ItemInstance copyInstance() {
        return new ItemInstance(this);
    } // Copy instance

    // Getters ---------------------------------------------------------------------------------------------------------
    public Item getItem() {
        return item;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getNotes() {
        return notes;
    }

    // Setters ---------------------------------------------------------------------------------------------------------
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
