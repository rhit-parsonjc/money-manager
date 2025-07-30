package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.moneymanager.api.requests.DateAmountUpdateRequest;

/*
A DateAmount records the amount of money in the account on a specified date.
*/

@Getter
@Entity
@Table(name="AMOUNTS")
@NoArgsConstructor
public class DateAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;

    private Long amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public DateAmount(Account account, Short yearValue, Byte monthValue,
                      Byte dayValue, Long amount) {
        this.account = account;
        this.yearValue = yearValue;
        this.monthValue = monthValue;
        this.dayValue = dayValue;
        this.amount = amount;
    }

    public void update(DateAmountUpdateRequest dateAmountUpdateRequest) {
        this.amount = dateAmountUpdateRequest.getAmount();
    }
}
