package com.moneymanager.api.models.test;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.models.FinancialTransaction;

import java.util.Set;

public class TestBankRecord extends BankRecord {
    public TestBankRecord(Long id, Account account, Short yearValue, Byte monthValue, Byte dayValue,
                          Long amount, String name, Set<FileAttachment> fileAttachments,
                          Set<FinancialTransaction> financialTransactions) {
        super(account, yearValue, monthValue, dayValue, amount, name);
        this.id = id;
        this.fileAttachments = fileAttachments;
        this.financialTransactions = financialTransactions;
    }
}
