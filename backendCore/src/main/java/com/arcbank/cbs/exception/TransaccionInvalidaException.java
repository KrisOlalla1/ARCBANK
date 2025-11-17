package com.arcbank.cbs.exception;

public class TransaccionInvalidaException extends RuntimeException {
    public TransaccionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
