package com.inventorymanagementsystem.nea.ims;

public class ItemInstance {
    private Item item;
    private int identifier;
    private String notes;
    private String location;

    public ItemInstance(Item item, int identifier, String notes, String location) {
        this.item = item;
        this.identifier = identifier;
        this.notes = notes;
        this.location = location;
    }

    public Item getItem() {
        return item;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getNotes() {
        return notes;
    }

    public String getLocation() {
        return location;
    }
}
