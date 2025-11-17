package com.bancaweb.bancaweb.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferenciaRequest {
    private Integer idUsuarioWeb; // Usuario logueado que inicia la transferencia
    private String cuentaOrigen;
    private String cuentaDestino;
    private BigDecimal monto;
    private String descripcion;
}