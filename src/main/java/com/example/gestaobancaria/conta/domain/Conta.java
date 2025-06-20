package com.example.gestaobancaria.conta.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private Long numeroConta;

    @Column(nullable = false)
    @NotNull
    private BigDecimal saldo;

    protected Conta() {}

    public Conta(Long numeroConta, BigDecimal saldo) {
        this.numeroConta = numeroConta;
        this.saldo = saldo;
    }

    public void debitar(BigDecimal valor) {
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na conta " + this.numeroConta);
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public Long getId() {
        return id;
    }

    public Long getNumeroConta() {
        return numeroConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
