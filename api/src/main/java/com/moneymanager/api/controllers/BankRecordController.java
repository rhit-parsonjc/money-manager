package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.requests.BankRecordRequest;
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
    public ResponseEntity createBankRecord(@RequestBody BankRecordRequest request) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.createBankRecord(request);
            return new ResponseEntity<BankRecordDto>(bankRecordDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getBankRecordById(@PathVariable Long id) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.getBankRecordById(id);
            return new ResponseEntity<BankRecordDto>(bankRecordDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity getBankRecords(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, @RequestParam(required = false) Integer day) {
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
                return new ResponseEntity<String>("Invalid query parameters", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<List<BankRecordDto>>(bankRecordDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBankRecord(@PathVariable Long id, @RequestBody BankRecordRequest request) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.updateBankRecord(id, request);
            return new ResponseEntity<BankRecordDto>(bankRecordDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBankRecord(@PathVariable Long id) {
        try {
            bankRecordService.deleteBankRecord(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity deleteBankRecords() {
        try {
            bankRecordService.deleteBankRecords();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
