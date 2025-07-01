package com.moneymanager.api.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.moneymanager.api.dtos.BankRecordDetailsDto;
import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.BankRecordService.BankRecordService;
import com.moneymanager.api.services.MapperService.MapperService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/bankrecords")
@RequiredArgsConstructor
public class BankRecordController {
    private final BankRecordService bankRecordService;
    private final MapperService mapperService;

    @PostMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> createBankRecord(
            @PathVariable Long accountId,
            @RequestBody BankRecordRequest request
    ) {
        BankRecord bankRecord = bankRecordService.createBankRecord(accountId, request);
        BankRecordDto bankRecordDto = mapperService.mapBankRecordToDto(bankRecord);
        DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("{accountId}/{id}")
    public ResponseEntity<DataOrErrorResponse> getBankRecordById(
            @PathVariable Long accountId,
            @PathVariable Long id
    ) {
        BankRecord bankRecord = bankRecordService.getBankRecordById(accountId, id);
        BankRecordDetailsDto bankRecordDetailsDto = mapperService.mapBankRecordToDetailsDto(bankRecord);
        DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDetailsDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @GetMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> getBankRecords(
            @PathVariable Long accountId,
            @RequestParam(required = false) Short year,
            @RequestParam(required = false) Byte month,
            @RequestParam(required = false) Byte day
    ) {
            List<BankRecord> bankRecords = null;
            if (year == null && month == null && day == null) {
                bankRecords = bankRecordService.getBankRecords(accountId);
            } else if (year != null && month == null && day == null) {
                bankRecords = bankRecordService.getBankRecordsForYear(accountId, year);
            } else if (year != null && month != null) {
                if (day == null)
                    bankRecords = bankRecordService.getBankRecordsForMonth(accountId, year, month);
                else
                    bankRecords = bankRecordService.getBankRecordsForDay(accountId, year, month, day);
            } else {
                DataOrErrorResponse response = new DataOrErrorResponse(false, "Invalid query parameters");
                return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.BAD_REQUEST);
            }
            List<BankRecordDto> bankRecordDtos = mapperService.mapBankRecordsToDtos(bankRecords);
            DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDtos);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @PutMapping("{accountId}/{id}")
    public ResponseEntity<DataOrErrorResponse> updateBankRecord(
            @PathVariable Long accountId,
            @PathVariable Long id,
            @RequestBody BankRecordRequest request
    ) {
        BankRecord bankRecord = bankRecordService.updateBankRecord(accountId, id, request);
        BankRecordDto bankRecordDto = mapperService.mapBankRecordToDto(bankRecord);
        DataOrErrorResponse response = new DataOrErrorResponse(true, bankRecordDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping("{accountId}/{id}")
    public ResponseEntity<DataOrErrorResponse> deleteBankRecord(
            @PathVariable Long accountId,
            @PathVariable Long id
    ) {
        bankRecordService.deleteBankRecord(accountId, id);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> deleteBankRecords(
            @PathVariable Long accountId
    ) {
        bankRecordService.deleteBankRecords(accountId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
