package com.moneymanager.api.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.moneymanager.api.requests.BankRecordRequest;

/*
A bank record represents a record from the financial institution.
A bank record has many financial transactions.
*/

@Getter
@Entity
@DiscriminatorValue(value = "R")
@NoArgsConstructor
public class BankRecord extends Item {
    @ManyToMany
    @JoinTable(
        name = "RECORD_TRANSACTIONS",
        joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "record_id", referencedColumnName = "id")
    )
    private Set<FinancialTransaction> financialTransactions;

    public BankRecord(Account account, Short yearValue, Byte monthValue,
                      Byte dayValue, Long amount, String name) {
        super(account, yearValue, monthValue, dayValue, amount, name);
        this.financialTransactions = new HashSet<FinancialTransaction>();
    }

    public void update(BankRecordRequest bankRecordRequest) {
        super.update(bankRecordRequest.getYearValue(),
                bankRecordRequest.getMonthValue(),
                bankRecordRequest.getDayValue(),
                bankRecordRequest.getAmount(),
                bankRecordRequest.getName());
    }
}
