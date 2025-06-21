package com.example.gestaobancaria.conta.domain;

import com.example.gestaobancaria.conta.api.dto.ContaRequestDTO;
import com.example.gestaobancaria.conta.api.dto.ContaResponseDTO;
import com.example.gestaobancaria.shared.exception.ContaJaExistenteException;
import com.example.gestaobancaria.shared.exception.ContaNaoEncontradaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @Test
    @DisplayName("Deve criar uma conta com sucesso")
    void deveCriarContaComSucesso() {
        long numeroConta = 123L;
        ContaRequestDTO requestDTO = new ContaRequestDTO(numeroConta, new BigDecimal("100.00"));
        when(contaRepository.existsByNumeroConta(numeroConta)).thenReturn(false);

        ContaResponseDTO responseDTO = contaService.criarConta(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(numeroConta, responseDTO.numeroConta());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar conta que já existe")
    void deveLancarExcecaoAoCriarContaExistente() {
        long numeroConta = 123L;
        ContaRequestDTO requestDTO = new ContaRequestDTO(numeroConta, new BigDecimal("100.00"));
        when(contaRepository.existsByNumeroConta(numeroConta)).thenReturn(true);

        assertThrows(ContaJaExistenteException.class, () -> contaService.criarConta(requestDTO));
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve buscar uma conta com sucesso")
    void deveBuscarContaComSucesso() {
        long numeroConta = 456L;
        Conta conta = new Conta(numeroConta, new BigDecimal("250.50"));
        when(contaRepository.findByNumeroConta(numeroConta)).thenReturn(Optional.of(conta));

        ContaResponseDTO responseDTO = contaService.buscarConta(numeroConta);

        assertNotNull(responseDTO);
        assertEquals(numeroConta, responseDTO.numeroConta());
        assertEquals(0, new BigDecimal("250.50").compareTo(responseDTO.saldo()));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar conta inexistente")
    void deveLancarExcecaoAoBuscarContaInexistente() {
        long numeroConta = 999L;
        when(contaRepository.findByNumeroConta(numeroConta)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> contaService.buscarConta(numeroConta));
    }
}