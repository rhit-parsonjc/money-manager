package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

@Getter
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

    public FileAttachment(String name, String type, Long size, byte[] contents) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.contents = contents;
        this.financialTransactions = new HashSet<FinancialTransaction>();
        this.bankRecords = new HashSet<BankRecord>();
    }

    @PreRemove
    private void disconnectBankRecordsAndFinancialTransactions() {
        for (FinancialTransaction financialTransaction : financialTransactions)
            financialTransaction.getFileAttachments().remove(this);
        for (BankRecord bankRecord : bankRecords)
            bankRecord.getFileAttachments().remove(this);
    }
}
