package com.moneymanager.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUsername(String username);
}
