package com.inventorymanagementsystem.nea.ims;

import com.inventorymanagementsystem.nea.ims.Classes.Validator;

public class TestMain {
    public static void main(String[] args) {

        String password4 = "Who!sTh4t^Guy";
        System.out.printf("Password 4 is %b \n", Validator.password(password4).isValid());
    }
}
