package com.moneymanager.api.models.test;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.DateAmount;

public class TestDateAmount extends DateAmount {
    public TestDateAmount(Long id, Account account, Short yearValue,
                          Byte monthValue, Byte dayValue, Long amount) {
        super(account, yearValue, monthValue, dayValue, amount);
        this.id = id;
    }
}
