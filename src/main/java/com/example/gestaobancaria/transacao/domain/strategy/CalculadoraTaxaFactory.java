package com.example.gestaobancaria.transacao.domain.strategy;

import com.example.gestaobancaria.shared.exception.FormaPagamentoInvalidaException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CalculadoraTaxaFactory {

    private final Map<Character, CalculadoraTaxaStrategy> strategyMap;

    public CalculadoraTaxaFactory(List<CalculadoraTaxaStrategy> strategies) {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(CalculadoraTaxaStrategy::getFormaPagamento, Function.identity()));
    }

    public CalculadoraTaxaStrategy getStrategy(char formaPagamento) {
        CalculadoraTaxaStrategy strategy = strategyMap.get(Character.toUpperCase(formaPagamento));
        if (strategy == null) {
            throw new FormaPagamentoInvalidaException("Forma de pagamento '" + formaPagamento + "' é inválida.");
        }
        return strategy;
    }
}
