package com.moneymanager.api.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public static final String ACCOUNT_MESSAGE = "Account not found";
    public static final String BANK_RECORD_MESSAGE = "Bank record not found";
    public static final String CONNECTION_MESSAGE = "Record-transaction connection not found";
    public static final String DATE_AMOUNT_MESSAGE = "Date amount not found";
    public static final String FILE_ATTACHMENT_MESSAGE = "File attachment not found";
    public static final String FINANCIAL_TRANSACTION_MESSAGE = "Financial transaction not found";
    public static final String ITEM_MESSAGE = "Item not found";
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
