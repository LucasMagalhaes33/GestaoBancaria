package com.example.gestaobancaria.transacao.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculadoraTaxaPix implements CalculadoraTaxaStrategy {


    @Override
    public char getFormaPagamento() {
        return 'P';
    }

    @Override
    public BigDecimal calcularValorComTaxa(BigDecimal valorOperacao) {
        return valorOperacao;
    }
}
