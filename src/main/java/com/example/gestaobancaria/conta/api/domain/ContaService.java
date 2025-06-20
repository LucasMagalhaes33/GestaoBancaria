package com.example.gestaobancaria.conta.api.domain;

import com.example.gestaobancaria.conta.api.dto.ContaRequestDTO;
import com.example.gestaobancaria.conta.api.dto.ContaResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public ContaResponseDTO criarConta(ContaRequestDTO contaRequestDTO) {
        if (contaRepository.existsByNumeroConta(contaRequestDTO.numeroConta())) {
            throw new IllegalArgumentException("A conta " + contaRequestDTO.numeroConta() + " já existe");
        }

        Conta novaConta = new Conta(contaRequestDTO.numeroConta(), contaRequestDTO.saldo());
        contaRepository.save(novaConta);
        return new ContaResponseDTO(novaConta.getNumeroConta(), novaConta.getSaldo());
    }

    public ContaResponseDTO buscarConta(Long numeroConta) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta " + numeroConta + " não encontrada."));
        return new ContaResponseDTO(conta.getNumeroConta(), conta.getSaldo());
    }

}
