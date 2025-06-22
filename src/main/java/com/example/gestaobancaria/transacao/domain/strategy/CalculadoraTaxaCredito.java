package com.example.gestaobancaria.transacao.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculadoraTaxaCredito implements CalculadoraTaxaStrategy {
    private static final BigDecimal TAXA_CREDITO = new BigDecimal("0.05");
    @Override
    public char getFormaPagamento() {
        return 'C';
    }

    @Override
    public BigDecimal calcularValorComTaxa(BigDecimal valorOperacao) {
        BigDecimal valorTaxa = valorOperacao.multiply(TAXA_CREDITO);
        return valorOperacao.add(valorTaxa).setScale(2, RoundingMode.HALF_UP);
    }
}
