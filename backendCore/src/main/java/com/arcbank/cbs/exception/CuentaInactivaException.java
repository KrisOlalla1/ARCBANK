package com.arcbank.cbs.exception;

public class CuentaInactivaException extends RuntimeException {
    public CuentaInactivaException(String numeroCuenta, String estado) {
        super("La cuenta " + numeroCuenta + " no está activa. Estado actual: " + estado);
    }
}
