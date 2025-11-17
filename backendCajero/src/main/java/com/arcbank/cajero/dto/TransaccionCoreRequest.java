package com.arcbank.cajero.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransaccionCoreRequest {
    private String numeroCuenta;
    private String tipo;
    private BigDecimal monto;
    private String descripcion;
    private String referencia;
    private Integer idSucursal;
}
