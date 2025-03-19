package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
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

    @PreRemove
    private void disconnectBankRecords() {
        for (BankRecord bankRecord : bankRecords) {
            bankRecord.getFinancialTransactions().remove(this);
        }
    }

    @ManyToMany
    private Set<FileAttachment> fileAttachments;
}
