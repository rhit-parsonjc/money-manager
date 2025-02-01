package com.moneymanager.api.services;

import com.moneymanager.api.dtos.DateAmountDto;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;

import java.util.List;

public interface DateAmountService {
    public DateAmountDto createDateRecord(DateAmountCreateRequest request);

    public List<DateAmountDto> getDateRecords();
    public List<DateAmountDto> getDateRecordsByYear(Integer year);
    public List<DateAmountDto> getDateRecordsByMonth(Integer year, Integer month);
    public List<DateAmountDto> getDateRecordByDay(Integer year, Integer month, Integer day);

    public DateAmountDto updateDateRecord(Integer year, Integer month, Integer day, DateAmountUpdateRequest request);

    public void deleteDateRecord(Integer year, Integer month, Integer day);
}
