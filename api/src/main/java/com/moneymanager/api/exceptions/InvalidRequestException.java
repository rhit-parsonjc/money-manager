package com.moneymanager.api.exceptions;

/*
An InvalidRequest exception indicates that the request has conflicting information.
*/

public class InvalidRequestException extends RuntimeException {
    public static final String INCORRECT_ACCOUNT = "Account does not match bank record's account";
    public static final String DIFFERENT_ACCOUNTS = "Bank record and financial transaction are part of different accounts";
    public InvalidRequestException(String message) {
        super(message);
    }
}
