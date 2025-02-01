package com.moneymanager.api.repositories;

import com.moneymanager.api.models.DateAmount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateAmountRepository extends JpaRepository<DateAmount, Long> {
    public List<DateAmount> findByYear(Integer year);
    public List<DateAmount> findByYearAndMonth(Integer year, Integer month);
    public List<DateAmount> findByYearAndMonthAndDay(Integer year, Integer month, Integer day);
}
