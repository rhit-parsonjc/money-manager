package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.moneymanager.api.requests.DateAmountUpdateRequest;

/**
 * A DateAmount represents a recorded amount of money on a particular date.
 * It contains the following properties:
 * - id
 * - year
 * - month
 * - day
 * - amount
 * - account
 */

@Getter
@Entity
@Table(name="AMOUNTS")
@NoArgsConstructor
public class DateAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Short year;
    private Byte month;
    private Byte day;

    private Long amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public DateAmount(Account account, Short year, Byte month, Byte day, Long amount) {
        this.account = account;
        this.year = year;
        this.month = month;
        this.day = day;
        this.amount = amount;
    }

    public void update(DateAmountUpdateRequest dateAmountUpdateRequest) {
        this.amount = dateAmountUpdateRequest.getAmount();
    }
}
