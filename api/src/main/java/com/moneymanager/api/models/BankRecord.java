package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
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
    Set<FinancialTransaction> financialTransactions;
}
