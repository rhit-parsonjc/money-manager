package com.moneymanager.api.exceptions;

public class InvalidStateException extends RuntimeException {
    public static final String ASYMMETRIC_CONNECTION = "Record-transaction connection incomplete";
    public static final String FILE_NO_LINKS = "File has no linked record or transaction";
    public static final String FILE_TWO_LINKS = "File has both a linked record and a linked transaction";
    public InvalidStateException(String message) {
        super(message);
    }
}
