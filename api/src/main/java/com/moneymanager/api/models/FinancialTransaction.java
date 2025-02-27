package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
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

    @ManyToMany
    Set<BankRecord> bankRecords;
}
