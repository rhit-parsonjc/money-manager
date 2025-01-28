package com.moneymanager.api.services;

import com.moneymanager.api.dtos.DateRecordDto;
import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.DateRecord;
import com.moneymanager.api.repositories.DateRecordRepository;
import com.moneymanager.api.requests.DateRecordCreateRequest;
import com.moneymanager.api.requests.DateRecordUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateRecordServiceImpl implements DateRecordService {
    private final DateRecordRepository dateRecordRepository;

    private DateRecordDto mapDateRecordToDto(DateRecord dateRecord) {
        DateRecordDto dateRecordDto = new DateRecordDto();
        dateRecordDto.setId(dateRecord.getId());
        dateRecordDto.setYear(dateRecord.getYear());
        dateRecordDto.setMonth(dateRecord.getMonth());
        dateRecordDto.setDay(dateRecord.getDay());
        dateRecordDto.setAmount(dateRecord.getAmount());
        return dateRecordDto;
    }

    private List<DateRecordDto> mapMultipleDateRecordsToDtos(List<DateRecord> dateRecords) {
        return dateRecords.stream().map(this::mapDateRecordToDto).toList();
    }

    private DateRecord mapDateRecordRequestToRecord(DateRecordCreateRequest dateRecordRequest) {
        DateRecord dateRecord = new DateRecord();
        dateRecord.setYear(dateRecordRequest.getYear());
        dateRecord.setMonth(dateRecordRequest.getMonth());
        dateRecord.setDay(dateRecordRequest.getDay());
        dateRecord.setAmount(dateRecordRequest.getAmount());
        return dateRecord;
    }

    private DateRecord getDateRecordByDayOrNull(Integer year, Integer month, Integer day) {
        List<DateRecord> dateRecords = dateRecordRepository.findByYearAndMonthAndDay(year, month, day);
        if (dateRecords.size() == 1) {
            return dateRecords.getFirst();
        } else {
            return null;
        }
    }

    @Override
    public DateRecordDto createDateRecord(DateRecordCreateRequest request) {
        DateRecord existingDateRecord = this.getDateRecordByDayOrNull(request.getYear(), request.getMonth(), request.getDay());
        if (existingDateRecord != null) {
            throw new AlreadyExistsException("Date record already exists");
        }
        DateRecord dateRecord = mapDateRecordRequestToRecord(request);
        DateRecord savedDateRecord = dateRecordRepository.save(dateRecord);
        return mapDateRecordToDto(savedDateRecord);
    }

    @Override
    public List<DateRecordDto> getDateRecords() {
        List<DateRecord> dateRecords = dateRecordRepository.findAll();
        return mapMultipleDateRecordsToDtos(dateRecords);
    }

    @Override
    public List<DateRecordDto> getDateRecordsByYear(Integer year) {
        List<DateRecord> dateRecords = dateRecordRepository.findByYear(year);
        return mapMultipleDateRecordsToDtos(dateRecords);
    }

    @Override
    public List<DateRecordDto> getDateRecordsByMonth(Integer year, Integer month) {
        List<DateRecord> dateRecords = dateRecordRepository.findByYearAndMonth(year, month);
        return mapMultipleDateRecordsToDtos(dateRecords);
    }

    @Override
    public DateRecordDto getDateRecordByDay(Integer year, Integer month, Integer day) {
        DateRecord dateRecord = getDateRecordByDayOrNull(year, month, day);
        if (dateRecord == null) {
            throw new ResourceNotFoundException("Date record not found");
        }
        return mapDateRecordToDto(dateRecord);
    }

    @Override
    public DateRecordDto updateDateRecord(Integer year, Integer month, Integer day, DateRecordUpdateRequest request) {
        DateRecord dateRecord = this.getDateRecordByDayOrNull(year, month, day);
        if (dateRecord == null) {
            throw new ResourceNotFoundException("Date record not found");
        }
        dateRecord.setAmount(request.getAmount());
        DateRecord savedDateRecord = dateRecordRepository.save(dateRecord);
        return mapDateRecordToDto(savedDateRecord);
    }

    @Override
    public void deleteDateRecord(Integer year, Integer month, Integer day) {
        DateRecord dateRecord = this.getDateRecordByDayOrNull(year, month, day);
        if (dateRecord == null) {
            throw new ResourceNotFoundException("Date record not found");
        }
        dateRecordRepository.deleteById(dateRecord.getId());
    }
}
