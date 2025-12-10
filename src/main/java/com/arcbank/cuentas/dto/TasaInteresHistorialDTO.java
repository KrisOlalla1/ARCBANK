package com.arcbank.cuentas.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class TasaInteresHistorialDTO {
    private Integer idTasaHistorial;
    private Integer idTipoCuenta;
    private java.math.BigDecimal tasaInteresAnual;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}