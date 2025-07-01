package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A FileAttachment contains the data for a file, which is linked to either a bank record
 * or a financial transaction, and has the following properties:
 * - id
 * - name
 * - type
 * - size
 * - contents
 * - financialTransaction
 * - bankRecord
 */

@Getter
@Entity
@Table(name="ATTACHMENTS")
@NoArgsConstructor
public class FileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Long size;

    @Lob
    private byte[] contents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id")
    private FinancialTransaction financialTransaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id")
    private BankRecord bankRecord;

    public FileAttachment(String name, String type, Long size, byte[] contents, FinancialTransaction financialTransaction) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.contents = contents;
        this.financialTransaction = financialTransaction;
        this.bankRecord = null;
    }

    public FileAttachment(String name, String type, Long size, byte[] contents, BankRecord bankRecord) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.contents = contents;
        this.financialTransaction = null;
        this.bankRecord = bankRecord;
    }
}
