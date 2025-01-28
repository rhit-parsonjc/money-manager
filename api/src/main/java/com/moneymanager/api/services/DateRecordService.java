package com.moneymanager.api.services;

import com.moneymanager.api.dtos.DateRecordDto;
import com.moneymanager.api.requests.DateRecordCreateRequest;
import com.moneymanager.api.requests.DateRecordUpdateRequest;

import java.util.List;

public interface DateRecordService {
    public DateRecordDto createDateRecord(DateRecordCreateRequest request);
    public List<DateRecordDto> getDateRecords();
    public List<DateRecordDto> getDateRecordsByYear(Integer year);
    public List<DateRecordDto> getDateRecordsByMonth(Integer year, Integer month);
    public DateRecordDto getDateRecordByDay(Integer year, Integer month, Integer day);
    public DateRecordDto updateDateRecord(Integer year, Integer month, Integer day, DateRecordUpdateRequest request);
    public void deleteDateRecord(Integer year, Integer month, Integer day);
}
