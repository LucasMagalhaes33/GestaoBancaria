package com.example.gestaobancaria.transacao.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum FormaPagamento {

    PIX('P', BigDecimal.ZERO),
    CREDITO('C', new BigDecimal("0.05")),
    DEBITO('D', new BigDecimal("0.03"));

    private final char sigla;
    private final BigDecimal taxa;

    FormaPagamento(char sigla, BigDecimal taxa) {
        this.sigla = sigla;
        this.taxa = taxa;
    }

    @JsonValue
    public char getSigla() {
        return sigla;
    }

    public BigDecimal calcularValorComTaxa(BigDecimal valorOperacao) {
        BigDecimal valorTaxa = valorOperacao.multiply(this.taxa);
        return valorOperacao.add(valorTaxa).setScale(2, RoundingMode.HALF_UP);
    }

}
