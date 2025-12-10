package com.arcbank.cuentas.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CuentaAhorroDTO {
    private Integer idCuenta;
    private String numeroCuenta;
    private Integer idCliente;
    private Integer idSucursalApertura;
    private Integer idTipoCuenta;
    private java.math.BigDecimal saldoActual;
    private java.math.BigDecimal saldoDisponible;
    private LocalDate fechaApertura;
    private LocalDateTime fechaUltimaTransaccion;
    private String estado;
}