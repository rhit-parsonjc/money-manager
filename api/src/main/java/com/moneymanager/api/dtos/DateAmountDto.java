package com.moneymanager.api.dtos;

import lombok.Data;

@Data
public class DateAmountDto {
    private Long id;
    private Integer year;
    private Integer month;
    private Integer day;
    private Double amount;
}
