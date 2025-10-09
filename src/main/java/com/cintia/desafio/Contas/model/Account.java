package com.cintia.desafio.Contas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tb_conta")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "valor_original", nullable = false)
    private BigDecimal originalValue;
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dueDate;
    @Column(name = "data_pagamento", nullable = false)
    private LocalDate paymentDate;
    @Column(name = "valor_ajustado", nullable = false)
    private BigDecimal adjustedAmount;
    @Column(name = "dias_de_atraso", nullable = false)
    private int daysLate;
}
