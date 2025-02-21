package com.inventorymanagementsystem.nea.ims;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static ValidationResult username(String username) {
        if (username == null) { // Checks if there is any username inputted
            return new ValidationResult(false, "Please enter a Username!");
        }
        if (username.length() < 5 || username.length() > 15) {
            return new ValidationResult(false, "Username must be between 5 and 15 characters long!");
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]+$");
        // Regex to check that username is alphanumeric and only contains _ or -
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            return new ValidationResult(false, "Username can only contain English letters, numbers, _ and -");
        }

        return new ValidationResult(true);
        // If it passed all these checks without throwing an exception then it passes as valid
    }

    public static ValidationResult password(String password){
        if (password == null) {
            return new ValidationResult(false, "Please enter a password!");
        }
        if (password.length() < 8 || password.length() > 25){
            return new ValidationResult(false,"Password must be between 8 and 25 characters long!");
        }

        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_+=,.?/-]).+$");
        // Regex that makes sure the password contains at least one of each of the criteria characters
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) {
            return new ValidationResult(false, "Password must contain at least one of each - lower/uppercase, number and special character");
        }

        pattern = Pattern.compile("^[A-Za-z0-9!@#$%^&*_+=,.?/-].+$");
        // This regex makes sure that there isn't any illegal characters
        matcher = pattern.matcher(password);
        if(!matcher.matches()) {
            return new ValidationResult(false, "Password can only contain letters, numbers, and the following special characters: !@#$%^&*_+-=,.?/");
        }

        return new ValidationResult(true);
    }

    public static ValidationResult general(String string){
        if (string.length() > 250){
            return new ValidationResult(false, "Description too long! Max 250 characters.");
        }
        Pattern pattern = Pattern.compile("^[A-Za-z0-9!@#$%^:;&*_+=,.?/()-].+$");
        Matcher matcher = pattern.matcher(string);
        if(!matcher.matches()){
            return new ValidationResult(false, "No Illegal characters!");
        }

        return new ValidationResult(true);
    }
}
