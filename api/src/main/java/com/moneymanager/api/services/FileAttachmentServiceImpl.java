package com.moneymanager.api.services;

import com.moneymanager.api.dtos.FileAttachmentDetailsDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.repositories.FileAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileAttachmentServiceImpl implements FileAttachmentService {
    private final FileAttachmentRepository fileAttachmentRepository;

    private FileAttachmentDto mapFileAttachmentToDto(FileAttachment fileAttachment) {
        FileAttachmentDto fileAttachmentDto = new FileAttachmentDto();
        fileAttachmentDto.setId(fileAttachment.getId());
        fileAttachmentDto.setName(fileAttachment.getName());
        fileAttachmentDto.setType(fileAttachment.getType());
        fileAttachmentDto.setSize(fileAttachment.getSize());
        return fileAttachmentDto;
    }

    private List<FileAttachmentDto> mapFileAttachmentsToDto(List<FileAttachment> fileAttachments) {
        return fileAttachments.stream().map(this::mapFileAttachmentToDto).toList();
    }

    @Override
    public FileAttachmentDto createFileAttachment(MultipartFile multipartFile) throws IOException {
        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setName(multipartFile.getOriginalFilename());
        fileAttachment.setType(multipartFile.getContentType());
        fileAttachment.setSize(multipartFile.getSize());
        fileAttachment.setContents(multipartFile.getBytes());
        FileAttachment savedFileAttachment = fileAttachmentRepository.save(fileAttachment);
        return mapFileAttachmentToDto(savedFileAttachment);
    }

    @Override
    public FileAttachmentDetailsDto getFileAttachment(Long id) throws SQLException {
        Optional<FileAttachment> fileAttachmentOptional = fileAttachmentRepository.findById(id);
        if (fileAttachmentOptional.isPresent()) {
            FileAttachment fileAttachment = fileAttachmentOptional.get();
            FileAttachmentDetailsDto fileAttachmentDetailsDto = new FileAttachmentDetailsDto();
            fileAttachmentDetailsDto.setName(fileAttachment.getName());
            fileAttachmentDetailsDto.setType(fileAttachment.getType());
            fileAttachmentDetailsDto.setContents(fileAttachment.getContents());
            return fileAttachmentDetailsDto;
        } else {
            throw new ResourceNotFoundException("File attachment not found");
        }
    }

    @Override
    public List<FileAttachmentDto> getFileAttachments() {
        return mapFileAttachmentsToDto(fileAttachmentRepository.findAll());
    }

    @Override
    public void deleteFileAttachment(Long id) {
        Optional<FileAttachment> fileAttachmentOptional = fileAttachmentRepository.findById(id);
        if (fileAttachmentOptional.isEmpty()) {
            throw new ResourceNotFoundException("File attachment not found");
        }
        fileAttachmentRepository.deleteById(id);
    }

    @Override
    public void attachBankRecord(Long fileId, Long recordId) {

    }

    @Override
    public void detachBankRecord(Long fileId, Long recordId) {

    }

    @Override
    public void attachFinancialTransaction(Long fileId, Long transactionId) {

    }

    @Override
    public void detachFinancialTransaction(Long fileId, Long transactionId) {

    }
}
