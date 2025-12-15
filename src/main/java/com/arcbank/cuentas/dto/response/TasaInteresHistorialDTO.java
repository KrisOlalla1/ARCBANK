package com.arcbank.cuentas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TasaInteresHistorialDTO {
    private Integer idTasaHistorial;
    private Integer idTipoCuenta;
    private BigDecimal tasaInteresAnual;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;
}
