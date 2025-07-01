package com.moneymanager.api.models;

import com.moneymanager.api.requests.AccountRequest;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An Account refers to a single bank account and has the following properties:
 * - id
 * - name
 * - userEntity
 * - bankRecords
 * - financialTransactions
 * - dateAmounts
 */

@Getter
@Entity
@Table(name="ACCOUNTS")
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    private Set<BankRecord> bankRecords;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    private Set<FinancialTransaction> financialTransactions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    private Set<DateAmount> dateAmounts;

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
