package com.moneymanager.api.services;

import com.moneymanager.api.dtos.DateAmountDto;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;

import java.util.List;

public interface DateAmountService {
    public DateAmountDto createDateAmount(DateAmountCreateRequest request);

    public List<DateAmountDto> getDateAmounts();
    public List<DateAmountDto> getDateAmountsByYear(Integer year);
    public List<DateAmountDto> getDateAmountsByMonth(Integer year, Integer month);
    public List<DateAmountDto> getDateAmountsByDay(Integer year, Integer month, Integer day);

    public DateAmountDto updateDateAmount(Integer year, Integer month, Integer day, DateAmountUpdateRequest request);

    public void deleteDateAmount(Integer year, Integer month, Integer day);
    public void deleteDateAmounts();
}
