package com.example.gestaobancaria.shared.exception;

public class ContaNaoEncontradaException extends RuntimeException{
    public ContaNaoEncontradaException(String message){
        super(message);
    }
}
