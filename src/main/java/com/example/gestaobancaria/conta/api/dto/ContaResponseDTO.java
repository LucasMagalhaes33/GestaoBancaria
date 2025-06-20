package com.example.gestaobancaria.conta.api.dto;

import java.math.BigDecimal;

public record ContaResponseDTO(
        Long numeroConta,
        BigDecimal saldo
) {
}
