package com.moneymanager.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.moneymanager.api.dtos.DateAmountDto;
import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.DateAmountService.DateAmountService;
import com.moneymanager.api.services.MapperService.MapperService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/dateamounts")
@RequiredArgsConstructor
public class DateAmountController {
    private final DateAmountService dateAmountService;
    private final MapperService mapperService;

    @PostMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> createDateAmount(
            @PathVariable Long accountId,
            @RequestBody DateAmountCreateRequest request
    ) {
        DateAmount dateAmount = dateAmountService.createDateAmount(accountId, request);
        DateAmountDto dateAmountDto = mapperService.mapDateAmountToDto(dateAmount);
        DataOrErrorResponse response = new DataOrErrorResponse(true, dateAmountDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @GetMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> getDateAmounts(
            @PathVariable Long accountId,
            @RequestParam(required = false) Short year,
            @RequestParam(required = false) Byte month,
            @RequestParam(required = false) Byte day
    ) {
        List<DateAmount> dateAmounts = null;
        if (year == null && month == null && day == null) {
            dateAmounts = dateAmountService.getDateAmounts(accountId);
        } else if (year != null && month == null & day == null) {
            dateAmounts = dateAmountService.getDateAmountsForYear(accountId, year);
        } else if (year != null && month != null) {
            if (day == null) {
                dateAmounts = dateAmountService.getDateAmountsForMonth(accountId, year, month);
            } else {
                dateAmounts = dateAmountService.getDateAmountsForDay(accountId, year, month, day);
            }
        }
        List<DateAmountDto> dateAmountDtos = mapperService.mapDateAmountsToDtos(dateAmounts);
        DataOrErrorResponse response = new DataOrErrorResponse(true, dateAmountDtos);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @PutMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> updateDateAmount(
            @PathVariable Long accountId,
            @RequestParam Short year,
            @RequestParam Byte month,
            @RequestParam Byte day,
            @RequestBody DateAmountUpdateRequest request
    ) {
        DateAmount dateAmount = dateAmountService.updateDateAmount(accountId, year, month, day, request);
        DateAmountDto dateAmountDto = mapperService.mapDateAmountToDto(dateAmount);
        DataOrErrorResponse response = new DataOrErrorResponse(true, dateAmountDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping("{accountId}")
    public ResponseEntity<DataOrErrorResponse> deleteDateAmount(
            @PathVariable Long accountId,
            @RequestParam Short year,
            @RequestParam Byte month,
            @RequestParam Byte day
    ) {
        dateAmountService.deleteDateAmount(accountId, year, month, day);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("")
    public ResponseEntity<DataOrErrorResponse> deleteDateAmounts(
            @RequestParam Long accountId
    ) {
        dateAmountService.deleteDateAmounts(accountId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
