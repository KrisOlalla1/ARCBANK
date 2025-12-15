package com.arcbank.cuentas.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class TipoCuentaAhorroRequest {

    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal tasaInteresMaxima;

    @NotBlank
    private String amortizacion;

    @NotNull
    private Boolean activo;
}
