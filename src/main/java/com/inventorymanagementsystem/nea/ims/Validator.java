package com.inventorymanagementsystem.nea.ims;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean username(String username) {
        if (username == null) { // Checks if there is any username inputted
            throw new IllegalArgumentException("Please enter a Username!");
        }
        if (username.length() < 5 || username.length() > 15) {
            throw new IllegalArgumentException("Username must be between 5 and 15 characters long!");
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]+$");
        // Regex to check that username is alphanumeric and only contains _ or -
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Username can only contain English letters, numbers, _ and -");
        }

        return true;
        // If it passed all these checks without throwing an exception then it passes as valid
    }

    public static boolean password(String password){
        if (password == null) {
            throw new IllegalArgumentException("Please enter a password!");
        }
        if (password.length() < 8 || password.length() > 25){
            throw new IllegalArgumentException("Password must be between 8 and 25 characters long!");
        }

        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_+=,.?/-]).+$");
        // Regex that makes sure the password contains at least one of each of the criteria characters
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Password must contain at least one of each - lower/uppercase, number and special character");
        }

        pattern = Pattern.compile("^[A-Za-z0-9!@#$%^&*_+=,.?/-].+$");
        // This regex makes sure that there isn't any illegal characters
        matcher = pattern.matcher(password);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Password can only contain letters, numbers, and the following special characters: !@#$%^&*_+-=,.?/");
        }

        return true;
        // TODO: implement better error handling, using `Boolean.parseBoolean` + remove some sus chars
    }

    public static boolean general(String string){
        if (string.length() > 250){
            throw new IllegalArgumentException("Description too long! Max 250 characters.");
        }
        Pattern pattern = Pattern.compile("^[A-Za-z0-9!@#$%^:;&*_+=,.?/()-].+$");
        Matcher matcher = pattern.matcher(string);
        if(!matcher.matches()){
            throw new IllegalArgumentException("No Illegal characters!");
        }

        return true;
    }
}
