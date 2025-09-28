package com.moneymanager.api.repositories;

import com.moneymanager.api.models.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUsername(String username);
    List<UserEntity> findByEmail(String email);
}
