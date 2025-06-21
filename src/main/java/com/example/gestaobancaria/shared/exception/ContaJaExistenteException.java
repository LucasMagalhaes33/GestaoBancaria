package com.example.gestaobancaria.shared.exception;

public class ContaJaExistenteException extends RuntimeException{
    public ContaJaExistenteException(String message){
        super(message);
    }
}
