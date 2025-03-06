package com.inventorymanagementsystem.nea.ims.Classes;

public class CustomFieldValue {
    private String title;
    private String value;

    public CustomFieldValue(String title, String value) {
        this.title = title;
        this.value = value;
    }

    // Getters ---------------------------------------------------------------------------------------------------------
    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    // Setters ---------------------------------------------------------------------------------------------------------
    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
