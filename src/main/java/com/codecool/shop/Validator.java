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

    public boolean validateRegistration(Map<String, String> regData, Map<String, String> res) {
        return regData != null && validateUsername(regData.get("name"), res) && validatePassword(regData.get("password"), res) && validateEMailAddress(regData.get("email"), res);
    }

    public boolean validateLogin(Map<String, String> loginData, Map<String, String> res) {
        return loginData != null && validateUsername(loginData.get("name"), res) && validatePassword(loginData.get("password"), res);
    }

    public boolean validateUserData(Map<String, String> userData, Map<String, String> res) {
        if (userData != null) {
            boolean validUsername = validateUsername(userData.get("userName"), res);
            boolean validAddress = validateAddress(userData.get("address"), res);
            boolean validCountry = validateCountry(userData.get("country"), res);
            boolean validEMailAddress = validateEMailAddress(userData.get("emailAddress"), res);
            boolean validZipCode = validateZipCode(userData.get("zipcode"), res);
            boolean validPhoneNumber = validatePhoneNumber(userData.get("phoneNumber"), res);
            return validUsername && validAddress && validCountry && validEMailAddress && validZipCode && validPhoneNumber;
        }
        return false;
    }

    public boolean validatePaymentData(Map<String, String> paymentData, Map<String, String> res) {
        if (paymentData != null) {
            if (paymentData.get("email") != null) {
                boolean validEmail = validateEMailAddress(paymentData.get("email"), res);
                boolean validPassword = validatePassword(paymentData.get("password"), res);
                return validEmail && validPassword;
            } else {
                if (paymentData.get("cardNumber") != null) {
                    boolean validCardHolderName = validateCardHolderName(paymentData.get("cardHolderName"), res);
                    boolean validCardNumber =  validateCardNumber(paymentData.get("cardNumber"), res);
                    boolean validCardCsc = validateCardCsc(paymentData.get("cscNumber"), res);
                    boolean validCardExpMonth = validateCardExpMonth(paymentData.get("expMonth"), res);
                    boolean validCardExpYear = validateCardExpYear(paymentData.get("expYear"), res);
                    return validCardCsc && validCardExpMonth && validCardNumber && validCardExpYear && validCardHolderName;
                }
                return false;
            }
        } else return false;
    }



    private boolean validateUsername(String username, Map<String, String> res){
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        if (!usernameMatcher.find()) {
            res.put("username", "Invalid username.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCountry(String country, Map<String, String> res){
        Pattern countryPattern = Pattern.compile("^[^0-9_!?\"]+$");
        Matcher countryMatcher = countryPattern.matcher(country);
        if (!countryMatcher.find()) {
            res.put("country", "Invalid country name.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateZipCode(String zipCode, Map<String, String> res){
        Pattern zipCodePattern = Pattern.compile("^[0-9]{4}$");
        Matcher zipCodeMatcher = zipCodePattern.matcher(zipCode);
        if (!zipCodeMatcher.find()) {
            res.put("zipCode", "Invalid ZIP code.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEMailAddress(String eMailAddress, Map<String, String> res){
        Pattern eMailPattern = Pattern.compile("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)");
        Matcher eMailMatcher = eMailPattern.matcher(eMailAddress);
        if (!eMailMatcher.find()) {
            res.put("email", "Invalid e-mail address.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePhoneNumber(String phoneNumber, Map<String, String> res){
        Pattern numberPattern = Pattern.compile("^[?0-9]+[0-9]+$");
        Matcher numberMatcher = numberPattern.matcher(phoneNumber);
        if (!numberMatcher.find()) {
            res.put("phoneNumber", "Invalid phone number.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAddress(String address, Map<String, String> res){
        Pattern addressPattern = Pattern.compile("^[^_!?$ß*>;]+$");
        Matcher addressMatcher = addressPattern.matcher(address);
        if (!addressMatcher.find()) {
            res.put("address", "Invalid address.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCardNumber(String cardNumber, Map<String, String> res){
        Pattern cardNumberPattern = Pattern.compile("^[0-9]{15,19}$");
        Matcher cardNumberMatcher = cardNumberPattern.matcher(cardNumber);
        if (!cardNumberMatcher.find()) {
            res.put("cardNumber", "Invalid card number.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCardExpMonth(String expMonth, Map<String, String> res){
        Pattern cardExpMonthPattern = Pattern.compile("^0[1-9]|1[0-2]$");
        Matcher cardExpMonthMatcher = cardExpMonthPattern.matcher(expMonth);
        if (!cardExpMonthMatcher.find()) {
            res.put("cardExpMonth", "Invalid card expiry month.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCardExpYear(String expYear, Map<String, String> res){
        Pattern cardExpYearPattern = Pattern.compile("^[0-9]{2}$");
        Matcher cardExpYearMatcher = cardExpYearPattern.matcher(expYear);
        if (!cardExpYearMatcher.find()) {
            res.put("cardExpYear", "Invalid card expiry year.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCardHolderName(String name, Map<String, String> res){
        Pattern cardCSCPattern = Pattern.compile("^[a-zA-Z]+[ ][a-zA-Z]+[ ]?[a-zA-Z]*$");
        Matcher cardCSCMatcher = cardCSCPattern.matcher(name);
        if (!cardCSCMatcher.find()) {
            res.put("cardHolderName", "Invalid card holder name.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCardCsc(String csc, Map<String, String> res){
        Pattern cardCSCPattern = Pattern.compile("^[0-9]{3}$");
        Matcher cardCSCMatcher = cardCSCPattern.matcher(csc);
        if (!cardCSCMatcher.find()) {
            res.put("cardCsc", "Invalid card csc.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword(String password, Map<String, String> res){
        Pattern passwordPattern = Pattern.compile("^[^_!?$ß*>;.]{5,}$");
        Matcher passwordMatcher = passwordPattern.matcher(password);
        if (!passwordMatcher.find()) {
            res.put("password", "Invalid password.");
            return false;
        } else {
            return true;
        }
    }
}
