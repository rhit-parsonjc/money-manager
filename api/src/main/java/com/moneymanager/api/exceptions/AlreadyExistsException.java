package com.moneymanager.api.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public static final String DATE_AMOUNT_MESSAGE = "Date amount already exists";
    public static final String CONNECTION_MESSAGE = "Connection message already exists";
    public AlreadyExistsException(String message) {
        super(message);
    }
}
