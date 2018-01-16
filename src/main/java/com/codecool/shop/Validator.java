package com.codecool.shop;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static Validator ourInstance = null;

    private Validator() {
    }

    public static Validator getInstance() {
        if (ourInstance == null) {
            ourInstance = new Validator();
        }
        return ourInstance;
    }

    public boolean validateUserData(Map<String, String> userData) {
        return userData != null && validateUsername(userData.get("userName")) && validateAddress(userData.get("address")) && validateCountry(userData.get("country")) && validateEMailAddress(userData.get("emailAddress")) && validatZipCode(userData.get("zipcode")) && validatePhoneNumber(userData.get("phoneNumber"));
    }

    private boolean validateUsername(String username){
        Pattern usernamePattern = Pattern.compile("[a-zA-Z_0-9]+");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.find();
    }

    private boolean validateCountry(String country){
        Pattern countryPattern = Pattern.compile("[^0-9_!?\"]+");
        Matcher countryMatcher = countryPattern.matcher(country);
        return countryMatcher.find();
    }

    private boolean validatZipCode(String zipCode){
        Pattern zipCodePattern = Pattern.compile("[0-9]{4}");
        Matcher zipCodeMatcher = zipCodePattern.matcher(zipCode);
        return zipCodeMatcher.find();
    }

    private boolean validateEMailAddress(String eMailAddress){
        Pattern eMailPattern = Pattern.compile("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)");
        Matcher eMailMatcher = eMailPattern.matcher(eMailAddress);
        return eMailMatcher.find();
    }

    private boolean validatePhoneNumber(String phoneNumber){
        Pattern numberPattern = Pattern.compile("[?0-9]+[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(phoneNumber);
        return numberMatcher.find();
    }

    private boolean validateAddress(String address){
        Pattern addressPattern = Pattern.compile("^[^_!?$ß*>;]+");
        Matcher addressMatcher = addressPattern.matcher(address);
        return addressMatcher.find();
    }
}
