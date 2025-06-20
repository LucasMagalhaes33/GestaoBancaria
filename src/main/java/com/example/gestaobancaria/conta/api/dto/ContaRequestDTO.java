package com.example.gestaobancaria.conta.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ContaRequestDTO(
        @NotNull(message = "O número da conta é obrigatório")
        @Positive(message = "O número da conta deve ser um valor positivo")
        Long numeroConta,

        @NotNull(message = "O saldo é obrigatório")
        @PositiveOrZero(message = "O saldo não pode ser negativo")
        BigDecimal saldo
) {
}
