package com.moneymanager.api.exceptions;

/**
 * An AlreadyExistsException is thrown when a resource already exists
 */

public class AlreadyExistsException extends RuntimeException {
    public static final String ACCOUNT_MESSAGE = "Account already exists";
    public static final String CONNECTION_MESSAGE = "Connection message already exists";
    public static final String DATE_AMOUNT_MESSAGE = "Date amount already exists";
    public static final String USER_MESSAGE = "User already exists";
    public AlreadyExistsException(String message) {
        super(message);
    }
}
