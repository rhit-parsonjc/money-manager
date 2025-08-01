package com.moneymanager.api.security;

import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserEntity> userEntities = userEntityRepository.findByUsername(username);
        if (userEntities.isEmpty())
            throw new UsernameNotFoundException("Username not found");
        UserEntity userEntity = userEntities.getFirst();
        return new AppUserDetails(userEntity);
    }
}
