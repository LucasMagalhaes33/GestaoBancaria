package com.example.gestaobancaria.shared.exception;

public class FormaPagamentoInvalidaException extends IllegalArgumentException {
    public FormaPagamentoInvalidaException(String message) {
        super(message);
    }
}
