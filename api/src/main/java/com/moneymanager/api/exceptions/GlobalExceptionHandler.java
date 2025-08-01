package com.moneymanager.api.exceptions;

import com.moneymanager.api.responses.DataOrErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/*
GlobalExceptionHandler responds with a
- NOT_FOUND status code for a ResourceNotFoundException
- CONFLICT status code for an AlreadyExistsException
- BAD_REQUEST status code for an InvalidRequestException
- FORBIDDEN status code for a PermissionsException
- INTERNAL_SERVER_ERROR status code for an InvalidStateException
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

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<DataOrErrorResponse> handleInvalidRequestException(InvalidRequestException e, WebRequest request) {
        DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PermissionsException.class)
    public ResponseEntity<DataOrErrorResponse> handlePermissionsException(PermissionsException e, WebRequest request) {
        DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<DataOrErrorResponse> handleInvalidStateException(InvalidStateException e, WebRequest request) {
        DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
