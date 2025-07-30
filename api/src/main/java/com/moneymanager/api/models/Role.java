package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
A Role specifies what permissions a user can have.
Currently, there is only one role: a USER role.
*/

@Getter
@Entity
@Table(name="ROLES")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private String name;

    public Role(String name) {
        this.name = name;
    }
}
