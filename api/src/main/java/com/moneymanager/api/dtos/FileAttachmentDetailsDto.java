package com.moneymanager.api.dtos;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
public class FileAttachmentDetailsDto {
    private String name;
    private String type;
    private byte[] contents;
}
