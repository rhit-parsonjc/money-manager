package com.moneymanager.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.FileAttachment;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    void deleteByBankRecordId(Long bankRecordId);
    void deleteByFinancialTransactionId(Long financialTransactionId);
}
