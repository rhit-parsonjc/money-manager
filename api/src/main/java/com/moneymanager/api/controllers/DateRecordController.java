package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.DateRecordDto;
import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.requests.DateRecordCreateRequest;
import com.moneymanager.api.requests.DateRecordUpdateRequest;
import com.moneymanager.api.services.DateRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/daterecord")
@RequiredArgsConstructor
public class DateRecordController {
    private final DateRecordService dateRecordService;

    @PostMapping("/")
    public ResponseEntity createDateRecord(@RequestBody DateRecordCreateRequest request) {
        try {
            DateRecordDto dateRecordDto = dateRecordService.createDateRecord(request);
            return new ResponseEntity<DateRecordDto>(dateRecordDto, HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity getDateRecords() {
        try {
            List<DateRecordDto> dateRecordDtos = dateRecordService.getDateRecords();
            return new ResponseEntity<List<DateRecordDto>>(dateRecordDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{year}")
    public ResponseEntity getDateRecordsByYear(@PathVariable Integer year) {
        try {
            List<DateRecordDto> dateRecordDtos = dateRecordService.getDateRecordsByYear(year);
            return new ResponseEntity<List<DateRecordDto>>(dateRecordDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity getDateRecordsByMonth(@PathVariable Integer year, @PathVariable Integer month) {
        try {
            List<DateRecordDto> dateRecordDtos = dateRecordService.getDateRecordsByMonth(year, month);
            return new ResponseEntity<List<DateRecordDto>>(dateRecordDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity getDateRecordsByDay(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        try {
            DateRecordDto dateRecordDto = dateRecordService.getDateRecordByDay(year, month, day);
            return new ResponseEntity<DateRecordDto>(dateRecordDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{year}/{month}/{day}")
    public ResponseEntity updateDateRecord(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, @RequestBody DateRecordUpdateRequest request) {
        try {
            DateRecordDto dateRecordDto = dateRecordService.updateDateRecord(year, month, day, request);
            return new ResponseEntity<DateRecordDto>(dateRecordDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{year}/{month}/{day}")
    public ResponseEntity deleteDateRecord(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        try {
            dateRecordService.deleteDateRecord(year, month, day);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
