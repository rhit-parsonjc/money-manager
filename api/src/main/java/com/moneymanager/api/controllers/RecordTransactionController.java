package com.moneymanager.api.controllers;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.RecordTransactionConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/recordtransactions")
@RequiredArgsConstructor
public class RecordTransactionController {
    private final RecordTransactionConnectionService recordTransactionConnectionService;

    @PostMapping("/{recordId}/{transactionId}")
    public ResponseEntity<DataOrErrorResponse> createConnection(@PathVariable Long recordId, @PathVariable Long transactionId) {
        try {
            recordTransactionConnectionService.createConnection(recordId, transactionId);
            DataOrErrorResponse response = new DataOrErrorResponse(true, null);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{recordId}/{transactionId}")
    public ResponseEntity<DataOrErrorResponse> deleteConnection(Long recordId, Long transactionId) {
        try {
            recordTransactionConnectionService.deleteConnection(recordId, transactionId);
            DataOrErrorResponse response = new DataOrErrorResponse(true, null);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
