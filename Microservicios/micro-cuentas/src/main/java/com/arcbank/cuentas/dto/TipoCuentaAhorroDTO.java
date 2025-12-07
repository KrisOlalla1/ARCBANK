package com.arcbank.cuentas.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCuentaAhorroDTO {

    private Integer idTipoCuenta;
    private String nombre;
    private String descripcion;
    private Double tasaInteresMaxima;
    private String amortizacion;
    private Boolean activo;
}
