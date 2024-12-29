package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.*;

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
}
