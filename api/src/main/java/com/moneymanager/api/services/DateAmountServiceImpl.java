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
    private final MapperService mapperService;

    private DateAmount getDateAmountByDayOrNull(Integer year, Integer month, Integer day) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonthAndDay(year, month, day);
        if (dateAmounts.size() == 1)
            return dateAmounts.getFirst();
        else
            return null;
    }

    @Override
    public DateAmountDto createDateAmount(DateAmountCreateRequest request) {
        DateAmount existingDateAmount = this.getDateAmountByDayOrNull(request.getYear(), request.getMonth(), request.getDay());
        if (existingDateAmount != null)
            throw new AlreadyExistsException(AlreadyExistsException.DATE_AMOUNT_MESSAGE);
        DateAmount dateAmount = mapperService.mapDateAmountRequestToAmount(request);
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount);
        return mapperService.mapDateAmountToDto(savedDateAmount);
    }

    @Override
    public List<DateAmountDto> getDateAmounts() {
        List<DateAmount> dateAmounts = dateAmountRepository.findAll();
        return mapperService.mapDateAmountsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateAmountsByYear(Integer year) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYear(year);
        return mapperService.mapDateAmountsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateAmountsByMonth(Integer year, Integer month) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonth(year, month);
        return mapperService.mapDateAmountsToDtos(dateAmounts);
    }

    @Override
    public List<DateAmountDto> getDateAmountsByDay(Integer year, Integer month, Integer day) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByYearAndMonthAndDay(year, month, day);
        return mapperService.mapDateAmountsToDtos(dateAmounts);
    }

    @Override
    public DateAmountDto updateDateAmount(Integer year, Integer month, Integer day, DateAmountUpdateRequest request) {
        DateAmount dateAmount = this.getDateAmountByDayOrNull(year, month, day);
        if (dateAmount == null)
            throw new ResourceNotFoundException(ResourceNotFoundException.DATE_AMOUNT_MESSAGE);
        dateAmount.update(request);
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount);
        return mapperService.mapDateAmountToDto(savedDateAmount);
    }

    @Override
    public void deleteDateAmount(Integer year, Integer month, Integer day) {
        DateAmount dateAmount = this.getDateAmountByDayOrNull(year, month, day);
        if (dateAmount == null)
            throw new ResourceNotFoundException(ResourceNotFoundException.DATE_AMOUNT_MESSAGE);
        dateAmountRepository.deleteById(dateAmount.getId());
    }

    @Override
    public void deleteDateAmounts() {
        dateAmountRepository.deleteAll();
    }
}
