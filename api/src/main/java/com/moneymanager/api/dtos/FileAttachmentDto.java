package com.moneymanager.api.dtos;

import lombok.Data;

@Data
public class FileAttachmentDto {
    private Long id;
    private String name;
    private String type;
    private long size;
}
