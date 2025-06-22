package com.example.gestaobancaria.transacao.domain;

import com.example.gestaobancaria.conta.api.dto.ContaResponseDTO;
import com.example.gestaobancaria.conta.domain.Conta;
import com.example.gestaobancaria.conta.domain.ContaRepository;
import com.example.gestaobancaria.shared.exception.ContaNaoEncontradaException;
import com.example.gestaobancaria.transacao.api.dto.TransacaoRequestDTO;
import com.example.gestaobancaria.transacao.domain.strategy.CalculadoraTaxaFactory;
import com.example.gestaobancaria.transacao.domain.strategy.CalculadoraTaxaStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransacaoService {
    private final ContaRepository contaRepository;
    private final CalculadoraTaxaFactory calculadoraTaxaFactory;

    public TransacaoService(ContaRepository contaRepository, CalculadoraTaxaFactory calculadoraTaxaFactory) {
        this.contaRepository = contaRepository;
        this.calculadoraTaxaFactory = calculadoraTaxaFactory;
    }

    public ContaResponseDTO realizarTransacao(TransacaoRequestDTO transacaoDTO) {
        Conta conta = contaRepository.findByNumeroConta(transacaoDTO.numero_conta())
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta " + transacaoDTO.numero_conta() + " não encontrada."));

        CalculadoraTaxaStrategy strategy = calculadoraTaxaFactory.getStrategy(transacaoDTO.forma_pagamento());


        BigDecimal valorDebitoTotal = strategy.calcularValorComTaxa(transacaoDTO.valor());

        conta.debitar(valorDebitoTotal);
        contaRepository.save(conta);

        return new ContaResponseDTO(conta.getNumeroConta(), conta.getSaldo());
    }
}
