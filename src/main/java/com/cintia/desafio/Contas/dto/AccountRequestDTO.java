package com.cintia.desafio.Contas.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AccountRequestDTO {
    @NotNull(message = "O nome não pode ser nulo.")
    @NotBlank(message = "O nome da conta é obrigatório")
    private String name;
    @NotNull(message = "O valor da conta é obrigatório")
    @Positive(message = "O valor da conta deve ser positivo.")
    private BigDecimal originalValue;
    @NotNull(message = "A data de vencimento da conta é obrigatório")
    private LocalDate dueDate;
    @NotNull(message = "A data de pagamento da conta é obrigatório")
    private LocalDate paymentDate;
}
