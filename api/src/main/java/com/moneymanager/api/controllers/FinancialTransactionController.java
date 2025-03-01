package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.dtos.FinancialTransactionDetailsDto;
import com.moneymanager.api.dtos.FinancialTransactionDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.FinancialTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/financialtransactions")
@RequiredArgsConstructor
public class FinancialTransactionController {
    private final FinancialTransactionService financialTransactionService;

    @PostMapping("")
    public ResponseEntity<DataOrErrorResponse> createFinancialTransaction(@RequestBody FinancialTransactionRequest request) {
        try {
            FinancialTransactionDto financialTransactionDto = financialTransactionService.createFinancialTransaction(request);
            DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataOrErrorResponse> getFinancialTransactionById(@PathVariable Long id) {
        try {
            FinancialTransactionDetailsDto financialTransactionDetailsDto = financialTransactionService.getFinancialTransactionById(id);
            DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDetailsDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<DataOrErrorResponse> getFinancialTransactions(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, @RequestParam(required = false) Integer day) {
        try {
            List<FinancialTransactionDto> financialTransactionDtos = null;
            if (year == null && month == null && day == null) {
                financialTransactionDtos = financialTransactionService.getFinancialTransactions();
            } else if (year != null && month == null && day == null) {
                financialTransactionDtos = financialTransactionService.getFinancialTransactionsForYear(year);
            } else if (year != null && month != null) {
                if (day == null) {
                    financialTransactionDtos = financialTransactionService.getFinancialTransactionsForMonth(year, month);
                } else {
                    financialTransactionDtos = financialTransactionService.getFinancialTransactionsForDay(year, month, day);
                }
            } else {
                DataOrErrorResponse response = new DataOrErrorResponse(false, "Invalid query parameters");
                return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.BAD_REQUEST);
            }
            DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDtos);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataOrErrorResponse> updateFinancialTransaction(@PathVariable Long id, @RequestBody FinancialTransactionRequest request) {
        try {
            FinancialTransactionDto financialTransactionDto = financialTransactionService.updateFinancialTransaction(id, request);
            DataOrErrorResponse response = new DataOrErrorResponse(true, financialTransactionDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataOrErrorResponse> deleteFinancialTransaction(@PathVariable Long id) {
        try {
            financialTransactionService.deleteFinancialTransaction(id);
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

    @DeleteMapping("")
    public ResponseEntity<DataOrErrorResponse> deleteFinancialTransactions() {
        try {
            financialTransactionService.deleteFinancialTransactions();
            DataOrErrorResponse response = new DataOrErrorResponse(true, null);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
