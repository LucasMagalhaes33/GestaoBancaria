package com.example.gestaobancaria.transacao.api;

import com.example.gestaobancaria.conta.api.dto.ContaResponseDTO;
import com.example.gestaobancaria.transacao.api.dto.TransacaoRequestDTO;
import com.example.gestaobancaria.transacao.domain.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacao")
@Tag(name = "Transações", description = "Endpoints para realização de operações financeiras")
public class TransacaoResource {
    private final TransacaoService transacaoService;

    public TransacaoResource(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @Operation(summary = "Realiza uma transação financeira", description = "Realiza um débito na conta (Pix, Débito ou Crédito), aplicando as taxas correspondentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada ou saldo insuficiente"),
            @ApiResponse(responseCode = "400", description = "Dados da transação inválidos")
    })
    @PostMapping
    public ResponseEntity<ContaResponseDTO> realizarTransacao(@Valid @RequestBody TransacaoRequestDTO transacaoRequestDTO) {
        ContaResponseDTO contaAtualizada = transacaoService.realizarTransacao(transacaoRequestDTO);
        return new ResponseEntity<>(contaAtualizada, HttpStatus.CREATED);
    }
}
