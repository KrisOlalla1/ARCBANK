package com.arcbank.cbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientosResponse {

    private String numeroCuenta;
    private Integer totalMovimientos;
    private List<MovimientoDetalle> movimientos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovimientoDetalle {
        private Integer idTransaccion;
        private LocalDateTime fechaHora;
        private String tipo;
        private String tipoMovimiento;
        private BigDecimal monto;
        private BigDecimal saldoPost;
        private String descripcion;
        private String referencia;
        private String estado;
    }
}
