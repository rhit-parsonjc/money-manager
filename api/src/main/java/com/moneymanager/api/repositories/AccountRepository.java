package com.moneymanager.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserEntityId(Long userEntityId);
    List<Account> findByUserEntityIdAndName(Long userEntityId, String name);
    void deleteByUserEntityId(Long userEntityId);
}
