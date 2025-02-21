package com.inventorymanagementsystem.nea.ims.Classes;

public class ItemInstance {
    private final Item item;
    private final int identifier;
    private final String notes;
    private final String location;

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
