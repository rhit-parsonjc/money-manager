package com.moneymanager.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.RecordTransactionConnectionService.RecordTransactionConnectionService;

@CrossOrigin
@RestController
@RequestMapping("api/v1/recordtransactions")
@RequiredArgsConstructor
public class RecordTransactionController {
    private final RecordTransactionConnectionService recordTransactionConnectionService;

    @PostMapping("{recordId}/{transactionId}")
    public ResponseEntity<DataOrErrorResponse> createConnection(
            @PathVariable Long recordId,
            @PathVariable Long transactionId
    ) {
        recordTransactionConnectionService.createConnection(recordId, transactionId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{recordId}/{transactionId}")
    public ResponseEntity<DataOrErrorResponse> deleteConnection(
            @PathVariable Long recordId,
            @PathVariable Long transactionId
    ) {
        recordTransactionConnectionService.deleteConnection(recordId, transactionId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
