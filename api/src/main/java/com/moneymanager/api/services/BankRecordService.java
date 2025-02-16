package com.moneymanager.api.services;

import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.requests.BankRecordRequest;

import java.util.List;

public interface BankRecordService {
    public BankRecordDto createBankRecord(BankRecordRequest request);

    public BankRecordDto getBankRecordById(Long id);
    public List<BankRecordDto> getBankRecords();
    public List<BankRecordDto> getBankRecordsForYear(Integer year);
    public List<BankRecordDto> getBankRecordsForMonth(Integer year, Integer month);
    public List<BankRecordDto> getBankRecordsForDay(Integer year, Integer month, Integer day);

    public BankRecordDto updateBankRecord(Long id, BankRecordRequest request);

    public void deleteBankRecord(Long id);
    public void deleteBankRecords();
}
