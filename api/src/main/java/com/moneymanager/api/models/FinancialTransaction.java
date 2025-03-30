package com.moneymanager.api.models;

import com.moneymanager.api.requests.FinancialTransactionRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class FinancialTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private Integer month;
    private Integer day;

    private Double amount;
    private String name;

    @ManyToMany(mappedBy = "financialTransactions")
    Set<BankRecord> bankRecords;

    @ManyToMany
    private Set<FileAttachment> fileAttachments;

    public FinancialTransaction(Integer year, Integer month, Integer day, Double amount, String name) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.amount = amount;
        this.name = name;
        this.bankRecords = new HashSet<BankRecord>();
        this.fileAttachments = new HashSet<FileAttachment>();
    }

    public void update(FinancialTransactionRequest financialTransactionRequest) {
        this.year = financialTransactionRequest.getYear();
        this.month = financialTransactionRequest.getMonth();
        this.day = financialTransactionRequest.getDay();
        this.name = financialTransactionRequest.getName();
    }

    @PreRemove
    private void disconnectBankRecords() {
        for (BankRecord bankRecord : bankRecords) {
            bankRecord.getFinancialTransactions().remove(this);
        }
    }
}
