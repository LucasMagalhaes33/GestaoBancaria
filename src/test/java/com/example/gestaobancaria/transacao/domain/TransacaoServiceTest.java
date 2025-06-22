package com.example.gestaobancaria.transacao.domain;

import com.example.gestaobancaria.conta.domain.Conta;
import com.example.gestaobancaria.conta.domain.ContaRepository;
import com.example.gestaobancaria.shared.exception.FormaPagamentoInvalidaException;
import com.example.gestaobancaria.transacao.api.dto.TransacaoRequestDTO;
import com.example.gestaobancaria.transacao.domain.strategy.CalculadoraTaxaCredito;
import com.example.gestaobancaria.transacao.domain.strategy.CalculadoraTaxaDebito;
import com.example.gestaobancaria.transacao.domain.strategy.CalculadoraTaxaFactory;
import com.example.gestaobancaria.transacao.domain.strategy.CalculadoraTaxaPix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private CalculadoraTaxaFactory calculadoraTaxaFactory;

    @InjectMocks
    private TransacaoService transacaoService;

    private static final Long NUMERO_CONTA_EXISTENTE = 123L;

    @Test
    @DisplayName("Deve usar a estratégia de Débito e calcular a taxa corretamente")
    void deveUsarEstrategiaDebitoCorretamente() {
        // Arrange
        Conta conta = new Conta(NUMERO_CONTA_EXISTENTE, new BigDecimal("200.00"));
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('D', NUMERO_CONTA_EXISTENTE, new BigDecimal("100.00"));

        when(contaRepository.findByNumeroConta(NUMERO_CONTA_EXISTENTE)).thenReturn(Optional.of(conta));
        // Usamos a instância real da estratégia para testar o cálculo de ponta a ponta
        when(calculadoraTaxaFactory.getStrategy('D')).thenReturn(new CalculadoraTaxaDebito());

        // Act
        transacaoService.realizarTransacao(transacaoDTO);

        // Assert
        // Saldo final esperado: 200.00 - (100.00 + 3%) = 97.00
        assertThat(conta.getSaldo()).isEqualByComparingTo("97.00");
    }

    @Test
    @DisplayName("Deve usar a estratégia de Crédito e calcular a taxa corretamente")
    void deveUsarEstrategiaCreditoCorretamente() {
        // Arrange
        Conta conta = new Conta(NUMERO_CONTA_EXISTENTE, new BigDecimal("200.00"));
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('C', NUMERO_CONTA_EXISTENTE, new BigDecimal("100.00"));

        when(contaRepository.findByNumeroConta(NUMERO_CONTA_EXISTENTE)).thenReturn(Optional.of(conta));
        when(calculadoraTaxaFactory.getStrategy('C')).thenReturn(new CalculadoraTaxaCredito());

        // Act
        transacaoService.realizarTransacao(transacaoDTO);

        // Assert
        // Saldo final esperado: 200.00 - (100.00 + 5%) = 95.00
        assertThat(conta.getSaldo()).isEqualByComparingTo("95.00");
    }

    @Test
    @DisplayName("Deve usar a estratégia de Pix e não aplicar taxa")
    void deveUsarEstrategiaPixCorretamente() {
        // Arrange
        Conta conta = new Conta(NUMERO_CONTA_EXISTENTE, new BigDecimal("200.00"));
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO('P', NUMERO_CONTA_EXISTENTE, new BigDecimal("100.00"));

        when(contaRepository.findByNumeroConta(NUMERO_CONTA_EXISTENTE)).thenReturn(Optional.of(conta));
        when(calculadoraTaxaFactory.getStrategy('P')).thenReturn(new CalculadoraTaxaPix());

        // Act
        transacaoService.realizarTransacao(transacaoDTO);

        // Assert
        // Saldo final esperado: 200.00 - 100.00 = 100.00
        assertThat(conta.getSaldo()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Deve lançar FormaPagamentoInvalidaException para forma de pagamento inexistente")
    void deveLancarExcecaoParaFormaDePagamentoInvalida() {
        // Arrange
        char formaPagamentoInvalida = 'X';
        TransacaoRequestDTO transacaoDTO = new TransacaoRequestDTO(formaPagamentoInvalida, NUMERO_CONTA_EXISTENTE, new BigDecimal("50.00"));

        when(contaRepository.findByNumeroConta(NUMERO_CONTA_EXISTENTE)).thenReturn(Optional.of(new Conta(NUMERO_CONTA_EXISTENTE, new BigDecimal("100"))));
        // Simulamos o comportamento da Factory lançando a nova exceção
        when(calculadoraTaxaFactory.getStrategy(formaPagamentoInvalida))
                .thenThrow(new FormaPagamentoInvalidaException("Forma de pagamento '" + formaPagamentoInvalida + "' é inválida."));

        // Act & Assert
        assertThatThrownBy(() -> transacaoService.realizarTransacao(transacaoDTO))
                .isInstanceOf(FormaPagamentoInvalidaException.class)
                .hasMessageContaining("Forma de pagamento '" + formaPagamentoInvalida + "' é inválida.");
    }
}