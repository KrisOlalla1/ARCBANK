package com.arcbank.cuentas.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaAhorroRequest {

    @NotBlank(message = "Número de cuenta obligatorio")
    @Size(min = 10, max = 20)
    private String numeroCuenta;

    @NotNull
    @Positive(message = "ID Cliente debe ser positivo")
    private Integer idCliente;

    @NotNull
    @Positive(message = "ID Sucursal debe ser positivo")
    private Integer idSucursalApertura;

    @NotNull
    @Positive(message = "ID TipoCuenta debe ser positivo")
    private Integer idTipoCuenta;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal saldoInicial;
}
