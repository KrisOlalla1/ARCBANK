package com.arcbank.cajero.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperacionCajeroRequest {
    private String numeroCuenta;
    private BigDecimal monto;
    private String descripcion;
    private Integer idCajero;
    private String referencia;
}
