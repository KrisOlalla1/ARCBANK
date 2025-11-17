package com.arcbank.cbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponse {

    private Integer idTransaccion;
    private String numeroCuenta;
    private String tipo;
    private BigDecimal monto;
    private BigDecimal balance;
    private LocalDateTime fechaHora;
    private String descripcion;
    private String referencia;
    private String estado;
    private String mensaje;
}
