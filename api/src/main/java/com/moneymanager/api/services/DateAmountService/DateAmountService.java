package com.moneymanager.api.services.DateAmountService;

import java.util.List;

import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;

public interface DateAmountService {
    DateAmount createDateAmount(Long accountId, DateAmountCreateRequest request);

    List<DateAmount> getDateAmounts(Long accountId);
    List<DateAmount> getDateAmountsByYear(Long accountId, Short yearValue);
    List<DateAmount> getDateAmountsByMonth(Long accountId, Short yearValue, Byte monthValue);
    List<DateAmount> getDateAmountsByDay(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);

    DateAmount updateDateAmount(Long accountId, Short yearValue, Byte monthValue, Byte dayValue, DateAmountUpdateRequest request);

    void deleteDateAmount(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);
    void deleteDateAmounts(Long accountId);
}
