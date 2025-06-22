package com.example.gestaobancaria.transacao.domain.strategy;

import java.math.BigDecimal;

public interface CalculadoraTaxaStrategy {

    char getFormaPagamento();

    BigDecimal calcularValorComTaxa(BigDecimal valorOperacao);

}
