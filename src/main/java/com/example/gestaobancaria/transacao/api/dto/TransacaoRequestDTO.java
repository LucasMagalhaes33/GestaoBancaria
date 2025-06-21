package com.example.gestaobancaria.transacao.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransacaoRequestDTO (
        @NotNull(message = "A forma de pagamento é obrigatória")
        char forma_pagamento,

        @NotNull(message = "O número da conta é obrigatório")
        Long numero_conta,

        @NotNull(message = "O valor da transação é obrigatório")
        @Positive(message = "O valor da transação deve ser positivo")
        BigDecimal valor
)
{}
