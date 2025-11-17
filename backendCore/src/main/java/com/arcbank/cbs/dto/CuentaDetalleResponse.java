package com.arcbank.cbs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDetalleResponse {
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoActual;
    private BigDecimal saldoDisponible;
    private String estado;
    private String tipoCliente; // "P" o "E"
    private String identificacion; // cedula o RUC
    private String nombres;
    private String apellidos;
    private String razonSocial;
    private boolean existe;
}
