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

    private DateAmountDto mapDateAmountToDto(DateAmount dateAmount) {
        DateAmountDto dateAmountDto = new DateAmountDto();
        dateAmountDto.setId(dateAmount.getId());
        dateAmountDto.setYear(dateAmount.getYear());
        dateAmountDto.setMonth(dateAmount.getMonth());
        dateAmountDto.setDay(dateAmount.getDay());
        dateAmountDto.setAmount(dateAmount.getAmount());
        return dateAmountDto;
    }

    private List<DateAmountDto> mapMultipleDateAmountsToDtos(List<DateAmount> dateAmounts) {
        return dateAmounts.stream().map(this::mapDateAmountToDto).toList();
    }

    private DateAmount mapDateAmountRequestToRecord(DateAmountCreateRequest dateRecordRequest) {
        DateAmount dateAmount = new DateAmount();
        dateAmount.setYear(dateRecordRequest.getYear());
        dateAmount.setMonth(dateRecordRequest.getMonth());
        dateAmount.setDay(dateRecordRequest.getDay());
        dateAmount.setAmount(dateRecordRequest.getAmount());
        return dateAmount;
    }

    private DateAmount getDateAmountByDayOrNull(Integer year, Integer month, Integer day) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonthAndDay(year, month, day);
        if (dateAmounts.size() == 1) {
            return dateAmounts.getFirst();
        } else {
            return null;
        }
    }

    @Override
    public DateAmountDto createDateAmount(DateAmountCreateRequest request) {
        DateAmount existingDateAmount = this.getDateAmountByDayOrNull(request.getYear(), request.getMonth(), request.getDay());
        if (existingDateAmount != null) {
            throw new AlreadyExistsException("Date record already exists");
        }
        DateAmount dateAmount = mapDateAmountRequestToRecord(request);
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount);
        return mapDateAmountToDto(savedDateAmount);
    }

    @Override
    public List<DateAmountDto> getDateAmounts() {
        List<DateAmount> dateAmounts = dateAmountRepository.findAll();
        return mapMultipleDateAmountsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateAmountsByYear(Integer year) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYear(year);
        return mapMultipleDateAmountsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateAmountsByMonth(Integer year, Integer month) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonth(year, month);
        return mapMultipleDateAmountsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateAmountsByDay(Integer year, Integer month, Integer day) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonthAndDay(year, month, day);
        return mapMultipleDateAmountsToDtos(dateAmounts);
    }

    @Override
    public DateAmountDto updateDateAmount(Integer year, Integer month, Integer day, DateAmountUpdateRequest request) {
        DateAmount dateAmount = this.getDateAmountByDayOrNull(year, month, day);
        if (dateAmount == null) {
            throw new ResourceNotFoundException("Date record not found");
        }
        dateAmount.setAmount(request.getAmount());
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount);
        return mapDateAmountToDto(savedDateAmount);
    }

    @Override
    public void deleteDateAmount(Integer year, Integer month, Integer day) {
        DateAmount dateAmount = this.getDateAmountByDayOrNull(year, month, day);
        if (dateAmount == null) {
            throw new ResourceNotFoundException("Date record not found");
        }
        dateAmountRepository.deleteById(dateAmount.getId());
    }

    @Override
    public void deleteDateAmounts() {
        dateAmountRepository.deleteAll();
    }
}
