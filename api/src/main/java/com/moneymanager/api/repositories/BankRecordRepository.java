package com.moneymanager.api.repositories;

import com.moneymanager.api.models.BankRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRecordRepository extends JpaRepository<BankRecord, Long> {
}
