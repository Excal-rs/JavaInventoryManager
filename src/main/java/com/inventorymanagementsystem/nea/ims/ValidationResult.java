package com.inventorymanagementsystem.nea.ims;

public class ValidationResult {
    private final boolean isValid;
    private final String reason;

    public ValidationResult(boolean isValid){
        this.isValid = isValid;
        this.reason = null;
    }

    public ValidationResult(boolean isValid, String reason){
        this.isValid = isValid;
        this.reason = reason;
    }

    public boolean isValid() {
        return isValid;
    }
    public String getReason(){
        return reason;
    }

}
