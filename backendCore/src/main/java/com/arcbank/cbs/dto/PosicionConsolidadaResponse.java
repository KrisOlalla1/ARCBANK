package com.arcbank.cbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosicionConsolidadaResponse {

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
    @NoArgsConstructor
    @AllArgsConstructor
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
