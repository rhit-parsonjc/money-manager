package com.moneymanager.api.models.test;

import com.moneymanager.api.models.*;

import java.util.Set;

public class TestAccount extends Account {
    public TestAccount(Long id, String name, UserEntity userEntity, Set<BankRecord> bankRecords,
                       Set<FinancialTransaction> financialTransactions, Set<DateAmount> dateAmounts) {
        super(userEntity, name);
        this.id = id;
        this.bankRecords = bankRecords;
        this.financialTransactions = financialTransactions;
        this.dateAmounts = dateAmounts;
    }
}
