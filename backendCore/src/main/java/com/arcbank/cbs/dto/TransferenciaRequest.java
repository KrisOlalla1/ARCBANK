package com.arcbank.cbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequest {

    @NotBlank(message = "El número de cuenta origen es obligatorio")
    private String numeroCuentaOrigen;

    @NotBlank(message = "El número de cuenta destino es obligatorio")
    private String numeroCuentaDestino;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    private String descripcion;

    private String referencia;

    private Integer idSucursalTx;
}
