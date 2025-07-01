package com.moneymanager.api.controllers;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.moneymanager.api.dtos.FileAttachmentDetailsDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.FileAttachmentService.FileAttachmentService;
import com.moneymanager.api.services.MapperService.MapperService;

@CrossOrigin
@RestController
@RequestMapping("api/v1/fileattachments")
@RequiredArgsConstructor
public class FileAttachmentController {
    private final FileAttachmentService fileAttachmentService;
    private final MapperService mapperService;

    @PostMapping("bankrecords/{recordId}")
    public ResponseEntity<DataOrErrorResponse> attachFileToBankRecord(
            @PathVariable Long recordId,
            @RequestParam("file") MultipartFile multipartFile
    ) {
        try {
            FileAttachment fileAttachment = fileAttachmentService.createFileAttachmentForBankRecord(multipartFile, recordId);
            FileAttachmentDto fileAttachmentDto = mapperService.mapFileAttachmentToDto(fileAttachment);
            DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("financialtransactions/{transactionId}")
    public ResponseEntity<DataOrErrorResponse> attachFileToFinancialTransaction(
            @PathVariable Long transactionId,
            @RequestParam("file") MultipartFile multipartFile
    ) {
        try {
            FileAttachment fileAttachment = fileAttachmentService.createFileAttachmentForFinancialTransaction(multipartFile, transactionId);
            FileAttachmentDto fileAttachmentDto = mapperService.mapFileAttachmentToDto(fileAttachment);
            DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFileAttachment(
            @PathVariable Long id
    ) {
        FileAttachment fileAttachment = fileAttachmentService.getFileAttachment(id);
        FileAttachmentDetailsDto fileAttachmentDetailsDto = mapperService.mapFileAttachmentToDetailsDto(fileAttachment);
        MediaType mediaType = MediaType.parseMediaType(fileAttachmentDetailsDto.getType());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileAttachmentDetailsDto.getName() + "\"")
                .body(fileAttachmentDetailsDto.getContents());
    }

    @GetMapping("bankrecords/{recordId}")
    public ResponseEntity<DataOrErrorResponse> getFileAttachmentsForBankRecord(
            @PathVariable Long recordId
    ) {
        List<FileAttachment> fileAttachmentList = fileAttachmentService.getFileAttachmentsByRecordId(recordId);
        List<FileAttachmentDto> fileAttachmentDtoList = mapperService.mapFileAttachmentsToDtos(fileAttachmentList);
        DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDtoList);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("financialtransactions/{transactionId}")
    public ResponseEntity<DataOrErrorResponse> getFileAttachmentsForFinancialTransaction(
            @PathVariable Long transactionId
    ) {
        List<FileAttachment> fileAttachmentList = fileAttachmentService.getFileAttachmentsByTransactionId(transactionId);
        List<FileAttachmentDto> fileAttachmentDtoList = mapperService.mapFileAttachmentsToDtos(fileAttachmentList);
        DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDtoList);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DataOrErrorResponse> deleteFileAttachment(
            @PathVariable Long id
    ) {
        fileAttachmentService.deleteFileAttachment(id);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("bankrecords/{recordId}")
    public ResponseEntity<DataOrErrorResponse> deleteFileAttachmentsForBankRecord(
            @PathVariable Long recordId
    ) {
        fileAttachmentService.deleteFileAttachmentsByRecordId(recordId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("financialtransactions/{transactionId}")
    public ResponseEntity<DataOrErrorResponse> deleteFileAttachmentsForFinancialTransaction(
            @PathVariable Long transactionId
    ) {
        fileAttachmentService.deleteFileAttachmentsByTransactionId(transactionId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
