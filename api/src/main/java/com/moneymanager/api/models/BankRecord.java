package com.moneymanager.api.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.moneymanager.api.requests.BankRecordRequest;

/**
 * A BankRecord refers to a single bank record and has the following properties:
 * - id
 * - year
 * - month
 * - day
 * - amount
 * - name
 * - account
 * - financialTransactions
 * - fileAttachments
 */

@Getter
@Entity
@Table(name="RECORDS")
@NoArgsConstructor
public class BankRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Short year;
    private Byte month;
    private Byte day;

    private Long amount;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany
    @JoinTable(
        name = "RECORD_TRANSACTIONS",
        joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "record_id", referencedColumnName = "id")
    )
    private Set<FinancialTransaction> financialTransactions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankRecord", orphanRemoval = true)
    private Set<FileAttachment> fileAttachments;

    public BankRecord(Account account, Short year, Byte month,
                      Byte day, Long amount, String name) {
        this.account = account;
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
