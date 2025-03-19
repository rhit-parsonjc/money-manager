package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Long size;

    @Lob
    private byte[] contents;

    @ManyToMany(mappedBy = "fileAttachments")
    private Set<FinancialTransaction> financialTransactions;

    @ManyToMany(mappedBy = "fileAttachments")
    private Set<BankRecord> bankRecords;

    @PreRemove
    private void disconnectBankRecordsAndFinancialTransactions() {
        for (FinancialTransaction financialTransaction : financialTransactions) {
            financialTransaction.getFileAttachments().remove(this);
        }
        for (BankRecord bankRecord : bankRecords) {
            bankRecord.getFileAttachments().remove(this);
        }
    }
}
