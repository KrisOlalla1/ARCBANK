package com.arcbank.cuentas.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
