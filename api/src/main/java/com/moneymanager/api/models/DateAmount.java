package com.moneymanager.api.models;

import com.moneymanager.api.requests.DateAmountUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class DateAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private Integer month;
    private Integer day;

    private Double amount;

    public DateAmount(Integer year, Integer month, Integer day, Double amount) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.amount = amount;
    }

    public void update(DateAmountUpdateRequest dateAmountUpdateRequest) {
        this.amount = dateAmountUpdateRequest.getAmount();
    }
}
