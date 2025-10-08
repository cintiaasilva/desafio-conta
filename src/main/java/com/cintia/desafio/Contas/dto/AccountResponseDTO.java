package com.cintia.desafio.Contas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class AccountResponseDTO {
    private String name;
    private BigDecimal originalValue;
    private LocalDate paymentDate;
    private BigDecimal adjustedValue;
    private Integer daysLate;
}
