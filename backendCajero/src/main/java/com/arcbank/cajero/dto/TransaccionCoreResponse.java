package com.arcbank.cajero.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransaccionCoreResponse {
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
