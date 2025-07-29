package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileAttachmentDto {
    private Long id;
    private String name;
    private String type;
    private Long size;
}
