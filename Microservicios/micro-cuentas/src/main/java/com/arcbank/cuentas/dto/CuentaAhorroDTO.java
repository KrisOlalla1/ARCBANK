package com.arcbank.cuentas.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaAhorroDTO {

    private Integer idCuenta;
    private String numeroCuenta;
    private Integer idCliente;
    private Integer idSucursalApertura;
    private Integer idTipoCuenta;
    private Double saldoActual;
    private Double saldoDisponible;
    private LocalDate fechaApertura;
    private LocalDateTime fechaUltimaTransaccion;
    private String estado;
}
