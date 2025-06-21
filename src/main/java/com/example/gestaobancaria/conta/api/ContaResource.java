package com.example.gestaobancaria.conta.api;

import com.example.gestaobancaria.conta.api.dto.ContaRequestDTO;
import com.example.gestaobancaria.conta.api.dto.ContaResponseDTO;
import com.example.gestaobancaria.conta.domain.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta")
@Tag(name = "Contas", description = "Endpoints para criação e consulta de contas")
public class ContaResource {

    private final ContaService contaService;

    public ContaResource(ContaService contaService) {
        this.contaService = contaService;
    }

    @Operation(summary = "Cria uma nova conta", description = "Cria uma nova conta com um número e saldo inicial.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou conta já existente")
    })
    @PostMapping
    public ResponseEntity<ContaResponseDTO> criarConta(@Valid @RequestBody ContaRequestDTO contaRequestDTO) {
        ContaResponseDTO novaConta = contaService.criarConta(contaRequestDTO);
        return new ResponseEntity<>(novaConta, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca uma conta", description = "Retorna os dados de uma conta pelo seu número.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping
    public ResponseEntity<ContaResponseDTO> buscarConta(@RequestParam Long numeroConta) {
        ContaResponseDTO conta = contaService.buscarConta(numeroConta);
        return ResponseEntity.ok(conta);
    }
}
