package com.moneymanager.api.services;

import com.moneymanager.api.dtos.DateAmountDto;
import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.repositories.DateAmountRepository;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateAmountServiceImpl implements DateAmountService {
    private final DateAmountRepository dateAmountRepository;

    private DateAmountDto mapDateRecordToDto(DateAmount dateAmount) {
        DateAmountDto dateAmountDto = new DateAmountDto();
        dateAmountDto.setId(dateAmount.getId());
        dateAmountDto.setYear(dateAmount.getYear());
        dateAmountDto.setMonth(dateAmount.getMonth());
        dateAmountDto.setDay(dateAmount.getDay());
        dateAmountDto.setAmount(dateAmount.getAmount());
        return dateAmountDto;
    }

    private List<DateAmountDto> mapMultipleDateRecordsToDtos(List<DateAmount> dateAmounts) {
        return dateAmounts.stream().map(this::mapDateRecordToDto).toList();
    }

    private DateAmount mapDateRecordRequestToRecord(DateAmountCreateRequest dateRecordRequest) {
        DateAmount dateAmount = new DateAmount();
        dateAmount.setYear(dateRecordRequest.getYear());
        dateAmount.setMonth(dateRecordRequest.getMonth());
        dateAmount.setDay(dateRecordRequest.getDay());
        dateAmount.setAmount(dateRecordRequest.getAmount());
        return dateAmount;
    }

    private DateAmount getDateRecordByDayOrNull(Integer year, Integer month, Integer day) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonthAndDay(year, month, day);
        if (dateAmounts.size() == 1) {
            return dateAmounts.getFirst();
        } else {
            return null;
        }
    }

    @Override
    public DateAmountDto createDateRecord(DateAmountCreateRequest request) {
        DateAmount existingDateAmount = this.getDateRecordByDayOrNull(request.getYear(), request.getMonth(), request.getDay());
        if (existingDateAmount != null) {
            throw new AlreadyExistsException("Date record already exists");
        }
        DateAmount dateAmount = mapDateRecordRequestToRecord(request);
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount);
        return mapDateRecordToDto(savedDateAmount);
    }

    @Override
    public List<DateAmountDto> getDateRecords() {
        List<DateAmount> dateAmounts = dateAmountRepository.findAll();
        return mapMultipleDateRecordsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateRecordsByYear(Integer year) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYear(year);
        return mapMultipleDateRecordsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateRecordsByMonth(Integer year, Integer month) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonth(year, month);
        return mapMultipleDateRecordsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateRecordByDay(Integer year, Integer month, Integer day) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonthAndDay(year, month, day);
        return mapMultipleDateRecordsToDtos(dateAmounts);
    }

    @Override
    public DateAmountDto updateDateRecord(Integer year, Integer month, Integer day, DateAmountUpdateRequest request) {
        DateAmount dateAmount = this.getDateRecordByDayOrNull(year, month, day);
        if (dateAmount == null) {
            throw new ResourceNotFoundException("Date record not found");
        }
        dateAmount.setAmount(request.getAmount());
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount);
        return mapDateRecordToDto(savedDateAmount);
    }

    @Override
    public void deleteDateRecord(Integer year, Integer month, Integer day) {
        DateAmount dateAmount = this.getDateRecordByDayOrNull(year, month, day);
        if (dateAmount == null) {
            throw new ResourceNotFoundException("Date record not found");
        }
        dateAmountRepository.deleteById(dateAmount.getId());
    }
}
