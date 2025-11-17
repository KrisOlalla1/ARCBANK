package com.bancaweb.bancaweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCoreDTO {

    private Integer idCuenta;
    private String numeroCuenta;
    private BigDecimal saldoActual;
    private String estadoCuenta;
    private String tipoCuenta;
    private String nombreCliente;
    private String identificacionCliente;
    private String sucursal;
}