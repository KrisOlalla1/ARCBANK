package com.arcbank.cuentas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoCuentaAhorroDTO {
    private Integer idTipoCuenta;
    private String nombre;
    private String descripcion;
    private java.math.BigDecimal tasaInteresMaxima;
    private String amortizacion;
    private Boolean activo;
}