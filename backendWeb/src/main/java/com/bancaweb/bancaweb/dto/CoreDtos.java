package com.bancaweb.bancaweb.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CoreDtos {
    @Data
    public static class ClienteValidacionResponse {
        private boolean existe;
        private Integer idCliente;
        private String identificacion;
        private String tipoCliente;
        private String nombreCompleto;
        private String estado;
        private String mensaje;
    }

    @Data
    public static class PosicionConsolidadaResponse {
        private Integer idCliente;
        private String nombreCliente;
        private String identificacion;
        private String tipoCliente;
        private BigDecimal saldoTotalActivo;
        private BigDecimal saldoTotalDisponible;
        private Integer totalCuentas;
        private LocalDate fechaConsulta;
        private List<CuentaResumen> cuentas;

        @Data
        public static class CuentaResumen {
            private Integer idCuenta;
            private String numeroCuenta;
            private String tipoCuenta;
            private BigDecimal saldoActual;
            private BigDecimal saldoDisponible;
            private String estado;
            private LocalDate fechaApertura;
            private String sucursal;
        }
    }

    @Data
    public static class CoreTransferRequest {
        private String numeroCuentaOrigen;
        private String numeroCuentaDestino;
        private BigDecimal monto;
        private String descripcion;
        private String referencia;
        private Integer idSucursalTx;
    }

    @Data
    public static class CoreTransaccionResponse {
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

    @Data
    public static class MovimientosResponse {
        private String numeroCuenta;
        private Integer totalMovimientos;
        private List<MovimientoDetalle> movimientos;

        @Data
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
}
