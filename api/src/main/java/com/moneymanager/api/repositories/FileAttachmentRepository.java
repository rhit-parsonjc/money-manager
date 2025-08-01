package com.moneymanager.api.repositories;

import com.moneymanager.api.models.FileAttachment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    List<FileAttachment> findByItemId(Long itemId);
    void deleteByItemId(Long itemId);
}
