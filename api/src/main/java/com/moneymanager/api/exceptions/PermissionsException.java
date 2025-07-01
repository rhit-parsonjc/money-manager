package com.moneymanager.api.exceptions;

/**
 * A PermissionsException indicates that the action is forbidden.
 */

public class PermissionsException extends RuntimeException {
    public static final String INCORRECT_USER = "User is not permitted to perform this action";
    public static final String NO_USER = "User does not exist";
    public PermissionsException(String message) {

        super(message);
    }
}
