package com.cintia.desafio.Contas.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Data
public class AccountRequestDTO {
    @NonNull
    private String name;
    @NonNull
    private BigDecimal originalValue;
    @NonNull
    private LocalDate dueDate;
    @NonNull
    private LocalDate paymentDate;
}
