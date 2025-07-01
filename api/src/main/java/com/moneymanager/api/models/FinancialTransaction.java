package com.moneymanager.api.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.moneymanager.api.requests.FinancialTransactionRequest;

/**
 * A FinancialTransaction refers to a financial transaction, which has the following properties:
 * - id
 * - year
 * - month
 * - day
 * - amount
 * - name
 * - account
 * - bankRecords
 * - fileAttachments
 */

@Getter
@Entity
@Table(name="TRANSACTIONS")
@NoArgsConstructor
public class FinancialTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Short year;
    private Byte month;
    private Byte day;

    private Long amount;
    private String name;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany(mappedBy = "financialTransactions")
    private Set<BankRecord> bankRecords;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "financialTransaction", orphanRemoval = true)
    private Set<FileAttachment> fileAttachments;

    public FinancialTransaction(Account account, Short year, Byte month, Byte day, Long amount, String name) {
        this.account = account;
        this.year = year;
        this.month = month;
        this.day = day;
        this.amount = amount;
        this.name = name;
        this.bankRecords = new HashSet<>();
        this.fileAttachments = new HashSet<>();
    }

    public void update(FinancialTransactionRequest financialTransactionRequest) {
        this.year = financialTransactionRequest.getYear();
        this.month = financialTransactionRequest.getMonth();
        this.day = financialTransactionRequest.getDay();
        this.name = financialTransactionRequest.getName();
        this.amount = financialTransactionRequest.getAmount();
    }

    @PreRemove
    private void disconnectBankRecords() {
        for (BankRecord bankRecord : bankRecords) {
            bankRecord.getFinancialTransactions().remove(this);
        }
    }
}
