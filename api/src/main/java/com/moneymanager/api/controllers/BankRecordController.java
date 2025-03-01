package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.BankRecordDetailsDto;
import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.BankRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/bankrecords")
@RequiredArgsConstructor
public class BankRecordController {
    private final BankRecordService bankRecordService;

    @PostMapping("")
    public ResponseEntity<DataOrErrorResponse> createBankRecord(@RequestBody BankRecordRequest request) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.createBankRecord(request);
            DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataOrErrorResponse> getBankRecordById(@PathVariable Long id) {
        try {
            BankRecordDetailsDto bankRecordDetailsDto = bankRecordService.getBankRecordById(id);
            DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDetailsDto);
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
    public ResponseEntity<DataOrErrorResponse> getBankRecords(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, @RequestParam(required = false) Integer day) {
        try {
            List<BankRecordDto> bankRecordDtos = null;
            if (year == null && month == null && day == null) {
                bankRecordDtos = bankRecordService.getBankRecords();
            } else if (year != null && month == null && day == null) {
                bankRecordDtos = bankRecordService.getBankRecordsForYear(year);
            } else if (year != null && month != null) {
                if (day == null) {
                    bankRecordDtos = bankRecordService.getBankRecordsForMonth(year, month);
                } else {
                    bankRecordDtos = bankRecordService.getBankRecordsForDay(year, month, day);
                }
            } else {
                DataOrErrorResponse response = new DataOrErrorResponse(false, "Invalid query parameters");
                return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.BAD_REQUEST);
            }
            DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDtos);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataOrErrorResponse> updateBankRecord(@PathVariable Long id, @RequestBody BankRecordRequest request) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.updateBankRecord(id, request);
            DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDto);
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
    public ResponseEntity<DataOrErrorResponse> deleteBankRecord(@PathVariable Long id) {
        try {
            bankRecordService.deleteBankRecord(id);
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
    public ResponseEntity<DataOrErrorResponse> deleteBankRecords() {
        try {
            bankRecordService.deleteBankRecords();
            DataOrErrorResponse response = new DataOrErrorResponse(true, null);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
