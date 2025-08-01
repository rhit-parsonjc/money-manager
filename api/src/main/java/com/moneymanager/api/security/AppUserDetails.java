package com.moneymanager.api.security;

import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AppUserDetails implements UserDetails {
    private String username;
    private String password;
    private List<GrantedAuthority> grantedAuthorities;

    public AppUserDetails(UserEntity userEntity) {
        username = userEntity.getUsername();
        password = userEntity.getPassword();
        Set<Role> roles = userEntity.getRoles();
        grantedAuthorities = roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
