package com.example.gestaobancaria.transacao.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculadoraTaxaDebito implements CalculadoraTaxaStrategy {
    private static final BigDecimal TAXA_DEBITO = new BigDecimal("0.03");

    @Override
    public char getFormaPagamento() {
        return 'D';
    }

    @Override
    public BigDecimal calcularValorComTaxa(BigDecimal valorOperacao) {
        BigDecimal valorTaxa = valorOperacao.multiply(TAXA_DEBITO);
        return valorOperacao.add(valorTaxa).setScale(2, RoundingMode.HALF_UP);
    }
}
