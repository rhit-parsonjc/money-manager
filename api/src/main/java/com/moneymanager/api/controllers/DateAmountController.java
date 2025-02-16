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
    public ResponseEntity createDateAmount(@RequestBody DateAmountCreateRequest request) {
        try {
            DateAmountDto dateAmountDto = dateAmountService.createDateAmount(request);
            return new ResponseEntity<DateAmountDto>(dateAmountDto, HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity getDateAmounts(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, @RequestBody(required = false) Integer day) {
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
            return new ResponseEntity<List<DateAmountDto>>(dateAmountDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{year}/{month}/{day}")
    public ResponseEntity updateDateAmount(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, @RequestBody DateAmountUpdateRequest request) {
        try {
            DateAmountDto dateAmountDto = dateAmountService.updateDateAmount(year, month, day, request);
            return new ResponseEntity<DateAmountDto>(dateAmountDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{year}/{month}/{day}")
    public ResponseEntity deleteDateAmount(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        try {
            dateAmountService.deleteDateAmount(year, month, day);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity deleteDateRecords() {
        try {
            dateAmountService.deleteDateAmounts();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
