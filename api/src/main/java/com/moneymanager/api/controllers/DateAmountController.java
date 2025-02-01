package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.DateAmountDto;
import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
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
    public ResponseEntity createDateRecord(@RequestBody DateAmountCreateRequest request) {
        try {
            DateAmountDto dateAmountDto = dateAmountService.createDateRecord(request);
            return new ResponseEntity<DateAmountDto>(dateAmountDto, HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity getDateRecords(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, @RequestBody(required = false) Integer day) {
        try {
            List<DateAmountDto> dateAmountDtos = null;
            if (year == null && month == null && day == null) {
                dateAmountDtos = dateAmountService.getDateRecords();
            } else if (year != null && month == null & day == null) {
                dateAmountDtos = dateAmountService.getDateRecordsByYear(year);
            } else if (year != null && month != null) {
                if (day == null) {
                    dateAmountDtos = dateAmountService.getDateRecordsByMonth(year, month);
                } else {
                    dateAmountDtos = dateAmountService.getDateRecordByDay(year, month, day);
                }
            }
            return new ResponseEntity<List<DateAmountDto>>(dateAmountDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{year}/{month}/{day}")
    public ResponseEntity updateDateRecord(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, @RequestBody DateAmountUpdateRequest request) {
        try {
            DateAmountDto dateAmountDto = dateAmountService.updateDateRecord(year, month, day, request);
            return new ResponseEntity<DateAmountDto>(dateAmountDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{year}/{month}/{day}")
    public ResponseEntity deleteDateRecord(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        try {
            dateAmountService.deleteDateRecord(year, month, day);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
