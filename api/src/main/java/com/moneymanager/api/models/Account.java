package com.moneymanager.api.models;

import com.moneymanager.api.requests.AccountRequest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/*
An account represents a single location for money, such as a bank account.
An account has many bank records, financial transactions, and date amounts.
*/

@Getter
@Entity
@Table(name="ACCOUNTS")
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    protected Set<BankRecord> bankRecords;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    protected Set<FinancialTransaction> financialTransactions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    protected Set<DateAmount> dateAmounts;

    public Account(UserEntity userEntity, String name) {
        this.userEntity = userEntity;
        this.name = name;
        this.bankRecords = new HashSet<>();
        this.financialTransactions = new HashSet<>();
        this.dateAmounts = new HashSet<>();
    }

    public void update(AccountRequest accountRequest) {
        this.name = accountRequest.getName();
    }
}
