package com.moneymanager.api.exceptions;

/**
 * A ResourceNotFoundException indicates that the resource does not exist
 */

public class ResourceNotFoundException extends RuntimeException {
    public static final String ACCOUNT_MESSAGE = "Account not found";
    public static final String BANK_RECORD_MESSAGE = "Bank record not found";
    public static final String CONNECTION_MESSAGE = "Record-transaction connection not found";
    public static final String DATE_AMOUNT_MESSAGE = "Date amount not found";
    public static final String FILE_ATTACHMENT_MESSAGE = "File attachment not found";
    public static final String FINANCIAL_TRANSACTION_MESSAGE = "Financial transaction not found";
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
