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

    public boolean validateRegistration(Map<String, String> regData) {
        return regData != null && validateUsername(regData.get("name")) && validatePassword(regData.get("password")) && validateEMailAddress(regData.get("email"));
    }

    public boolean validateLogin(Map<String, String> loginData) {
        return loginData != null && validateUsername(loginData.get("name")) && validatePassword(loginData.get("password"));
    }

    public boolean validateUserData(Map<String, String> userData) {
        return userData != null && validateUsername(userData.get("userName")) && validateAddress(userData.get("address")) && validateCountry(userData.get("country")) && validateEMailAddress(userData.get("emailAddress")) && validateZipCode(userData.get("zipcode")) && validatePhoneNumber(userData.get("phoneNumber"));
    }

    public boolean validatePaymentData(Map<String, String> paymentData) {
        if (paymentData != null) {
            if (paymentData.get("email") != null) {
                return validateEMailAddress(paymentData.get("email")) && validatePassword(paymentData.get("password"));
            } else {
                return paymentData.get("cardNumber") != null && validateCardNumber(paymentData.get("cardNumber")) && validateCardCsc(paymentData.get("cscNumber")) && validateCardExpMonth(paymentData.get("expMonth")) && validateCardExpYear(paymentData.get("expYear"));
            }
        } else return false;
    }


    private boolean validateUsername(String username){
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.find();
    }

    private boolean validateCountry(String country){
        Pattern countryPattern = Pattern.compile("^[^0-9_!?\"]+$");
        Matcher countryMatcher = countryPattern.matcher(country);
        return countryMatcher.find();
    }

    private boolean validateZipCode(String zipCode){
        Pattern zipCodePattern = Pattern.compile("^[0-9]{4}$");
        Matcher zipCodeMatcher = zipCodePattern.matcher(zipCode);
        return zipCodeMatcher.find();
    }

    private boolean validateEMailAddress(String eMailAddress){
        Pattern eMailPattern = Pattern.compile("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)");
        Matcher eMailMatcher = eMailPattern.matcher(eMailAddress);
        return eMailMatcher.find();
    }

    private boolean validatePhoneNumber(String phoneNumber){
        Pattern numberPattern = Pattern.compile("^[?0-9]+[0-9]+$");
        Matcher numberMatcher = numberPattern.matcher(phoneNumber);
        return numberMatcher.find();
    }

    private boolean validateAddress(String address){
        Pattern addressPattern = Pattern.compile("^[^_!?$ß*>;]+$");
        Matcher addressMatcher = addressPattern.matcher(address);
        return addressMatcher.find();
    }

    private boolean validateCardNumber(String cardNumber){
        Pattern cardNumberPattern = Pattern.compile("^[0-9]{15,19}$");
        Matcher cardNumberMatcher = cardNumberPattern.matcher(cardNumber);
        return cardNumberMatcher.find();
    }

    private boolean validateCardExpMonth(String expMonth){
        Pattern cardExpMonthPattern = Pattern.compile("^0[1-9]|1[0-2]$");
        Matcher cardExpMonthMatcher = cardExpMonthPattern.matcher(expMonth);
        return cardExpMonthMatcher.find();
    }

    private boolean validateCardExpYear(String expYear){
        Pattern cardExpYearPattern = Pattern.compile("^[0-9]{2}$");
        Matcher cardExpYearMatcher = cardExpYearPattern.matcher(expYear);
        return cardExpYearMatcher.find();
    }

    private boolean validateCardCsc(String csc){
        Pattern cardCSCPattern = Pattern.compile("^[0-9]{3}$");
        Matcher cardCSCMatcher = cardCSCPattern.matcher(csc);
        return cardCSCMatcher.find();
    }

    private boolean validatePassword(String password){
        Pattern passwordPattern = Pattern.compile("^[^_!?$ß*>;.]{5,}$");
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.find();
    }
}
