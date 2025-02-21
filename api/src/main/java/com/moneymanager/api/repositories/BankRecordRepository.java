package com.moneymanager.api.repositories;

import com.moneymanager.api.models.BankRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRecordRepository extends JpaRepository<BankRecord, Long> {
    public List<BankRecord> findByYear(Integer year);
    public List<BankRecord> findByYearAndMonth(Integer year, Integer month);
    public List<BankRecord> findByYearAndMonthAndDay(Integer year, Integer month, Integer day);
}
