package com.example.gestaobancaria.transacao.domain;

import com.example.gestaobancaria.conta.api.dto.ContaResponseDTO;
import com.example.gestaobancaria.conta.domain.Conta;
import com.example.gestaobancaria.conta.domain.ContaRepository;
import com.example.gestaobancaria.shared.exception.ContaNaoEncontradaException;
import com.example.gestaobancaria.transacao.api.dto.TransacaoRequestDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
public class TransacaoService {
    private final ContaRepository contaRepository;

    public TransacaoService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public ContaResponseDTO realizarTransacao(TransacaoRequestDTO transacaoDTO) {
        Conta conta = contaRepository.findByNumeroConta(transacaoDTO.numero_conta())
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta " + transacaoDTO.numero_conta() + " não encontrada."));

        FormaPagamento formaPagamento = Stream.of(FormaPagamento.values())
                .filter(fp -> fp.getSigla() == transacaoDTO.forma_pagamento())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Forma de pagamento inválida: " + transacaoDTO.forma_pagamento()));

        BigDecimal valorDebitoTotal = formaPagamento.calcularValorComTaxa(transacaoDTO.valor());

        conta.debitar(valorDebitoTotal);
        contaRepository.save(conta);

        return new ContaResponseDTO(conta.getNumeroConta(), conta.getSaldo());
    }
}
