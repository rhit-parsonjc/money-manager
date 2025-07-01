package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A Role specifies what actions a user can perform.
 * There is currently only a single role: USER
 */

@Getter
@Entity
@Table(name="ROLES")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Role(String name) {
        this.name = name;
    }
}
