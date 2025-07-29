package com.moneymanager.api.exceptions;

public class InvalidStateException extends RuntimeException {
    public static final String ASYMMETRIC_CONNECTION = "Record-transaction connection incomplete";
    public InvalidStateException(String message) {
        super(message);
    }
}
