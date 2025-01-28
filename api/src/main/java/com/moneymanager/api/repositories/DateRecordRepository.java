package com.moneymanager.api.repositories;

import com.moneymanager.api.models.DateRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateRecordRepository extends JpaRepository<DateRecord, Long> {
    public List<DateRecord> findByYear(Integer year);
    public List<DateRecord> findByYearAndMonth(Integer year, Integer month);
    public List<DateRecord> findByYearAndMonthAndDay(Integer year, Integer month, Integer day);
}
