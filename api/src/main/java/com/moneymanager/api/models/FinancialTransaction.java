package com.moneymanager.api.models;

import com.moneymanager.api.requests.FinancialTransactionRequest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/*
A financial transaction represents a record not from the financial institution.
A financial transaction has many bank records.
*/

@Getter
@Entity
@DiscriminatorValue(value = "T")
@NoArgsConstructor
public class FinancialTransaction extends Item {
    @ManyToMany(mappedBy = "financialTransactions")
    protected Set<BankRecord> bankRecords;

    public FinancialTransaction(Account account, Short yearValue, Byte monthValue,
                                Byte dayValue, Long amount, String name) {
        super(account, yearValue, monthValue, dayValue, amount, name);
        this.bankRecords = new HashSet<>();
    }

    public void update(FinancialTransactionRequest financialTransactionRequest) {
        super.update(financialTransactionRequest.getYearValue(),
                financialTransactionRequest.getMonthValue(),
                financialTransactionRequest.getDayValue(),
                financialTransactionRequest.getAmount(),
                financialTransactionRequest.getName());
    }

    @PreRemove
    private void disconnectBankRecords() {
        for (BankRecord bankRecord : bankRecords) {
            bankRecord.getFinancialTransactions().remove(this);
        }
    }
}
