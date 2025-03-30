package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
@AllArgsConstructor
public class FileAttachmentDetailsDto {
    private Long id;
    private String name;
    private String type;
    private Long size;
    private byte[] contents;
}
