package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/*
An item represents either a bank record or financial transaction.
An item has many file attachments.
*/

@Getter
@Entity
@Table(name="ITEMS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
@NoArgsConstructor
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;
    private Long amount;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
    protected Set<FileAttachment> fileAttachments;

    public Item(Account account, Short yearValue, Byte monthValue, Byte dayValue, Long amount, String name) {
        this.account = account;
        this.yearValue = yearValue;
        this.monthValue = monthValue;
        this.dayValue = dayValue;
        this.amount = amount;
        this.name = name;
        this.fileAttachments = new HashSet<FileAttachment>();
    }

    protected void update(Short yearValue, Byte monthValue, Byte dayValue, Long amount, String name) {
        this.yearValue = yearValue;
        this.monthValue = monthValue;
        this.dayValue = dayValue;
        this.amount = amount;
        this.name = name;
    }
}
