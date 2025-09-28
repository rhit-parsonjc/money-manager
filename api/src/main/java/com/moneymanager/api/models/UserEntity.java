package com.moneymanager.api.models;

import com.moneymanager.api.requests.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/*
A UserEntity represents the data for a user.
A UserEntity has many roles and accounts.
*/
@Getter
@Entity
@Table(name="USERS")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="USER_ROLES",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity", orphanRemoval = true)
    protected Set<Account> accounts;

    public UserEntity(String email, String firstName, String lastName, String username, String password, Set<Role> roles) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.accounts = new HashSet<>();
    }

    public void update(UserUpdateRequest userUpdateRequest) {
        String newEmail = userUpdateRequest.getEmail();
        String newFirstName = userUpdateRequest.getFirstName();
        String newLastName = userUpdateRequest.getLastName();
        String newUsername = userUpdateRequest.getUsername();
        if (newEmail != null)
            this.email = newEmail;
        if (newFirstName != null)
            this.firstName = newFirstName;
        if (newLastName != null)
            this.lastName = newLastName;
        if (newUsername != null)
            this.username = newUsername;
    }

    public void update(UserUpdateRequest userUpdateRequest, String encodedPassword) {
        this.update(userUpdateRequest);
        this.password = encodedPassword;
    }
}
