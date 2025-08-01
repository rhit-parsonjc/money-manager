package com.moneymanager.api.repositories;

import com.moneymanager.api.models.Account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserEntityId(Long userEntityId);
    List<Account> findByUserEntityIdAndName(Long userEntityId, String name);
    void deleteByUserEntityId(Long userEntityId);
}
