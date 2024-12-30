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
@RequestMapping("/api/v1/bankrecord")
@RequiredArgsConstructor
public class BankRecordController {
    private final BankRecordService bankRecordService;

    @PostMapping("/")
    public ResponseEntity createBankRecord(@RequestBody BankRecordRequest request) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.createBankRecord(request);
            return ResponseEntity.ok(bankRecordDto);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getBankRecordById(@PathVariable Long id) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.getBankRecordById(id);
            return ResponseEntity.ok(bankRecordDto);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity getBankRecords() {
        List<BankRecordDto> bankRecordDtos = bankRecordService.getBankRecords();
        return ResponseEntity.ok(bankRecordDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBankRecord(@PathVariable Long id, @RequestBody BankRecordRequest request) {
        try {
            BankRecordDto bankRecordDto = bankRecordService.updateBankRecord(id, request);
            return ResponseEntity.ok(bankRecordDto);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBankRecord(@PathVariable Long id) {
        bankRecordService.deleteBankRecord(id);
        return ResponseEntity.ok("Deletion successful");
    }
}
