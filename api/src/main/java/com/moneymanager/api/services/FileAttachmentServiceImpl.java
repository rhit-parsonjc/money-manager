package com.moneymanager.api.services;

import com.moneymanager.api.dtos.FileAttachmentDetailsDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FileAttachmentRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FileAttachmentServiceImpl implements FileAttachmentService {
    private final FileAttachmentRepository fileAttachmentRepository;
    private final BankRecordRepository bankRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final MapperService mapperService;

    @Override
    public FileAttachment createFileAttachment(MultipartFile multipartFile) throws IOException {
        return new FileAttachment(
            multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), multipartFile.getBytes()
        );
    }

    @Override
    public FileAttachmentDto attachFileToBankRecord(MultipartFile file, Long recordId) throws IOException {
        FileAttachment fileAttachment = createFileAttachment(file);
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(recordId);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        fileAttachment.getBankRecords().add(bankRecord);
        FileAttachment savedFileAttachment = fileAttachmentRepository.save(fileAttachment);
        bankRecord.getFileAttachments().add(savedFileAttachment);
        bankRecordRepository.save(bankRecord);
        return mapperService.mapFileAttachmentToDto(savedFileAttachment);
    }

    @Override
    public FileAttachmentDto attachFileToFinancialTransaction(MultipartFile file, Long transactionId) throws IOException {
        FileAttachment fileAttachment = createFileAttachment(file);
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(transactionId);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        fileAttachment.getFinancialTransactions().add(financialTransaction);
        FileAttachment savedFileAttachment = fileAttachmentRepository.save(fileAttachment);
        financialTransaction.getFileAttachments().add(savedFileAttachment);
        financialTransactionRepository.save(financialTransaction);
        return mapperService.mapFileAttachmentToDto(savedFileAttachment);
    }


    @Override
    public FileAttachmentDetailsDto getFileAttachment(Long id) {
        Optional<FileAttachment> fileAttachmentOptional = fileAttachmentRepository.findById(id);
        if (fileAttachmentOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FILE_ATTACHMENT_MESSAGE);
        FileAttachment fileAttachment = fileAttachmentOptional.get();
        return mapperService.mapFileAttachmentToDetailsDto(fileAttachment);
    }

    @Override
    public List<FileAttachmentDto> getFileAttachments() {
        return mapperService.mapFileAttachmentsToDtos(fileAttachmentRepository.findAll());
    }

    @Override
    public List<FileAttachmentDto> getFileAttachmentsByRecordId(Long recordId) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(recordId);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        return mapperService.mapFileAttachmentsToDtos(bankRecord.getFileAttachments().stream().toList());
    }

    @Override
    public List<FileAttachmentDto> getFileAttachmentsByTransactionId(Long transactionId) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(transactionId);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        return mapperService.mapFileAttachmentsToDtos(financialTransaction.getFileAttachments().stream().toList());
    }

    @Override
    public void deleteFileAttachment(Long id) {
        Optional<FileAttachment> fileAttachmentOptional = fileAttachmentRepository.findById(id);
        if (fileAttachmentOptional.isEmpty())
            throw new ResourceNotFoundException("File attachment not found");
        fileAttachmentRepository.deleteById(id);
    }

    @Override
    public void deleteFileAttachments() {
        fileAttachmentRepository.deleteAll();
    }
}
