package com.example.gestaobancaria.transacao.domain;

import com.example.gestaobancaria.conta.api.dto.ContaResponseDTO;
import com.example.gestaobancaria.conta.domain.Conta;
import com.example.gestaobancaria.conta.domain.ContaRepository;
import com.example.gestaobancaria.shared.exception.ContaNaoEncontradaException;
import com.example.gestaobancaria.shared.exception.SaldoInsuficienteException;
import com.example.gestaobancaria.transacao.api.dto.TransacaoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Test
    @DisplayName("Deve realizar transação com débito e descontar taxa de 3%")
    void deveRealizarTransacaoDebitoComSucesso() {
        long numeroConta = 123L;
        Conta conta = new Conta(numeroConta, new BigDecimal("200.00"));
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('D', numeroConta, new BigDecimal("100.00"));

        when(contaRepository.findByNumeroConta(numeroConta)).thenReturn(Optional.of(conta));

        ContaResponseDTO contaResponse = transacaoService.realizarTransacao(transacaoDTO);

        assertNotNull(contaResponse);
        assertEquals(0, new BigDecimal("97.00").compareTo(contaResponse.saldo()));
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve realizar transação com crédito e descontar taxa de 5%")
    void deveRealizarTransacaoCreditoComSucesso() {
        long numeroConta = 123L;
        Conta conta = new Conta(numeroConta, new BigDecimal("200.00"));
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('C', numeroConta, new BigDecimal("100.00"));

        when(contaRepository.findByNumeroConta(numeroConta)).thenReturn(Optional.of(conta));

        ContaResponseDTO contaResponse = transacaoService.realizarTransacao(transacaoDTO);

        assertNotNull(contaResponse);
        assertEquals(0, new BigDecimal("95.00").compareTo(contaResponse.saldo()));
    }

    @Test
    @DisplayName("Deve realizar transação com Pix sem descontar taxa")
    void deveRealizarTransacaoPixComSucesso() {
        long numeroConta = 123L;
        Conta conta = new Conta(numeroConta, new BigDecimal("200.00"));
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('P', numeroConta, new BigDecimal("100.00"));

        when(contaRepository.findByNumeroConta(numeroConta)).thenReturn(Optional.of(conta));

        ContaResponseDTO contaResponse = transacaoService.realizarTransacao(transacaoDTO);

        assertNotNull(contaResponse);
        assertEquals(0, new BigDecimal("100.00").compareTo(contaResponse.saldo()));
    }

    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException quando saldo for menor que o valor com taxas")
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        long numeroConta = 456L;
        Conta conta = new Conta(numeroConta, new BigDecimal("100.00"));
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('D', numeroConta, new BigDecimal("100.00"));

        when(contaRepository.findByNumeroConta(numeroConta)).thenReturn(Optional.of(conta));

        assertThrows(SaldoInsuficienteException.class, () ->
            transacaoService.realizarTransacao(transacaoDTO)
        );

        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve lançar ContaNaoEncontradaException quando a conta não existir")
    void deveLancarExcecaoQuandoContaNaoEncontrada() {
        long numeroConta = 999L;
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('P', numeroConta, new BigDecimal("50.00"));

        when(contaRepository.findByNumeroConta(numeroConta)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () ->
            transacaoService.realizarTransacao(transacaoDTO)
        );
    }
}