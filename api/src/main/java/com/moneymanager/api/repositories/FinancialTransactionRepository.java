package com.moneymanager.api.repositories;

import com.moneymanager.api.models.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {
    public List<FinancialTransaction> findByYear(Integer year);
    public List<FinancialTransaction> findByYearAndMonth(Integer year, Integer month);
    public List<FinancialTransaction> findByYearAndMonthAndDay(Integer year, Integer month, Integer day);
}
