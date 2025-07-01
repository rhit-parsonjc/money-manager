package com.moneymanager.api.exceptions;

import com.moneymanager.api.responses.DataOrErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * The GlobalExceptionHandler handles the following exceptions when raised from a controller:
 * - ResourceNotFoundException
 * - AlreadyExistsException
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DataOrErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<DataOrErrorResponse> handleAlreadyExistsException(AlreadyExistsException e, WebRequest request) {
        DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CONFLICT);
    }
}
