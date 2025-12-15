package com.arcbank.cuentas.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TasaInteresHistorialRequest {

    @NotNull
    @Positive
    private Integer idTipoCuenta;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal tasaInteresAnual;

    @NotNull
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
