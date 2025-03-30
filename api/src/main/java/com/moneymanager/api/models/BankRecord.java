package com.moneymanager.api.models;

import com.moneymanager.api.requests.BankRecordRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class BankRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private Integer month;
    private Integer day;

    private Double amount;
    private String name;

    @ManyToMany
    private Set<FinancialTransaction> financialTransactions;

    @ManyToMany
    private Set<FileAttachment> fileAttachments;

    public BankRecord(Integer year, Integer month, Integer day, Double amount, String name) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.amount = amount;
        this.name = name;
        this.financialTransactions = new HashSet<FinancialTransaction>();
        this.fileAttachments = new HashSet<FileAttachment>();
    }

    public void update(BankRecordRequest bankRecordRequest) {
        this.year = bankRecordRequest.getYear();
        this.month = bankRecordRequest.getMonth();
        this.day = bankRecordRequest.getDay();
        this.amount = bankRecordRequest.getAmount();
        this.name = bankRecordRequest.getName();
    }
}
