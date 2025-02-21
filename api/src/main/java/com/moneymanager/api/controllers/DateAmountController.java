package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.DateAmountDto;
import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.DateAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/dateamounts")
@RequiredArgsConstructor
public class DateAmountController {
    private final DateAmountService dateAmountService;

    @PostMapping("")
    public ResponseEntity<DataOrErrorResponse> createDateAmount(@RequestBody DateAmountCreateRequest request) {
        try {
            DateAmountDto dateAmountDto = dateAmountService.createDateAmount(request);
            DataOrErrorResponse response = new DataOrErrorResponse(true, dateAmountDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, "Unknown server error occurred");
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<DataOrErrorResponse> getDateAmounts(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, @RequestBody(required = false) Integer day) {
        try {
            List<DateAmountDto> dateAmountDtos = null;
            if (year == null && month == null && day == null) {
                dateAmountDtos = dateAmountService.getDateAmounts();
            } else if (year != null && month == null & day == null) {
                dateAmountDtos = dateAmountService.getDateAmountsByYear(year);
            } else if (year != null && month != null) {
                if (day == null) {
                    dateAmountDtos = dateAmountService.getDateAmountsByMonth(year, month);
                } else {
                    dateAmountDtos = dateAmountService.getDateAmountsByDay(year, month, day);
                }
            }
            DataOrErrorResponse response = new DataOrErrorResponse(true, dateAmountDtos);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, "Unknown server error occurred");
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{year}/{month}/{day}")
    public ResponseEntity<DataOrErrorResponse> updateDateAmount(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, @RequestBody DateAmountUpdateRequest request) {
        try {
            DateAmountDto dateAmountDto = dateAmountService.updateDateAmount(year, month, day, request);
            DataOrErrorResponse response = new DataOrErrorResponse(true, dateAmountDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, "Unknown server error occurred");
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{year}/{month}/{day}")
    public ResponseEntity<DataOrErrorResponse> deleteDateAmount(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        try {
            dateAmountService.deleteDateAmount(year, month, day);
            DataOrErrorResponse response = new DataOrErrorResponse(true, null);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, "Unknown server error occurred");
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<DataOrErrorResponse> deleteDateRecords() {
        try {
            dateAmountService.deleteDateAmounts();
            DataOrErrorResponse response = new DataOrErrorResponse(true, null);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, "Unknown server error occurred");
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
