package com.moneymanager.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.DateAmount;

public interface DateAmountRepository extends JpaRepository<DateAmount, Long> {
    List<DateAmount> findByAccountId(Long accountId);
    List<DateAmount> findByAccountIdAndYear(Long accountId, Short year);
    List<DateAmount> findByAccountIdAndYearAndMonth(Long accountId, Short year, Byte month);
    List<DateAmount> findByAccountIdAndYearAndMonthAndDay(Long accountId, Short year, Byte month, Byte day);
    void deleteByAccountId(Long accountId);
}
