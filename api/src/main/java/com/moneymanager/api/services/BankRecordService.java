package com.moneymanager.api.services;

import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.requests.BankRecordRequest;

import java.util.List;

public interface BankRecordService {
    public BankRecordDto createBankRecord(BankRecordRequest request);
    public BankRecordDto getBankRecordById(Long id);
    public List<BankRecordDto> getBankRecords();
    public BankRecordDto updateBankRecord(Long id, BankRecordRequest request);
    public void deleteBankRecord(Long id);
}
