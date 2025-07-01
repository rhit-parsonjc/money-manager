package com.moneymanager.api.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.moneymanager.api.dtos.FinancialTransactionDetailsDto;
import com.moneymanager.api.dtos.FinancialTransactionDto;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.FinancialTransactionService.FinancialTransactionService;
import com.moneymanager.api.services.MapperService.MapperService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/financialtransactions")
@RequiredArgsConstructor
public class FinancialTransactionController {
    private final FinancialTransactionService financialTransactionService;
    private final MapperService mapperService;

    @PostMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> createFinancialTransaction(
            @PathVariable Long accountId,
            @RequestBody FinancialTransactionRequest request
    ) {
        FinancialTransaction financialTransaction = financialTransactionService.createFinancialTransaction(accountId, request);
        FinancialTransactionDto financialTransactionDto = mapperService.mapFinancialTransactionToDto(financialTransaction);
        DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("{accountId}/{id}")
    public ResponseEntity<DataOrErrorResponse> getFinancialTransactionById(
            @PathVariable Long accountId,
            @PathVariable Long id
    ) {
        FinancialTransaction financialTransaction = financialTransactionService.getFinancialTransactionById(accountId, id);
        FinancialTransactionDetailsDto financialTransactionDetailsDto = mapperService.mapFinancialTransactionToDetailsDto(financialTransaction);
        DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDetailsDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @GetMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> getFinancialTransactions(
            @PathVariable Long accountId,
            @RequestParam(required = false) Short year,
            @RequestParam(required = false) Byte month,
            @RequestParam(required = false) Byte day
    ) {
        List<FinancialTransaction> financialTransactions = null;
        if (year == null && month == null && day == null) {
            financialTransactions = financialTransactionService.getFinancialTransactions(accountId);
        } else if (year != null && month == null && day == null) {
            financialTransactions = financialTransactionService.getFinancialTransactionsForYear(accountId, year);
        } else if (year != null && month != null) {
            if (day == null) {
                financialTransactions = financialTransactionService.getFinancialTransactionsForMonth(accountId, year, month);
            } else {
                financialTransactions = financialTransactionService.getFinancialTransactionsForDay(accountId, year, month, day);
            }
        } else {
            DataOrErrorResponse response = new DataOrErrorResponse(false, "Invalid query parameters");
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.BAD_REQUEST);
        }
        List<FinancialTransactionDto> financialTransactionDtos = mapperService.mapFinancialTransactionsToDtos(financialTransactions);
        DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDtos);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @PutMapping("{accountId}/{id}")
    public ResponseEntity<DataOrErrorResponse> updateFinancialTransaction(
            @PathVariable Long accountId,
            @PathVariable Long id,
            @RequestBody FinancialTransactionRequest request
    ) {
        FinancialTransaction financialTransaction = financialTransactionService.updateFinancialTransaction(accountId, id, request);
        FinancialTransactionDto financialTransactionDto = mapperService.mapFinancialTransactionToDto(financialTransaction);
        DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping("{accountId}/{id}")
    public ResponseEntity<DataOrErrorResponse> deleteFinancialTransaction(
            @PathVariable Long accountId,
            @PathVariable Long id
    ) {
        financialTransactionService.deleteFinancialTransaction(accountId, id);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> deleteFinancialTransactions(
            @PathVariable Long accountId
    ) {
        financialTransactionService.deleteFinancialTransactions(accountId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
