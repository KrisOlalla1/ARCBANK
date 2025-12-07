package com.arcbank.cuentas.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"CuentaAhorro\"", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IdCuenta\"")
    private Integer idCuenta;

    @Column(name = "\"NumeroCuenta\"", length = 20, nullable = false, unique = true)
    private String numeroCuenta;

    // REFERENCIAS LÓGICAS (no FK) a otros microservicios
    @Column(name = "\"IdCliente\"", nullable = false)
    private Integer idCliente;

    @Column(name = "\"IdSucursalApertura\"", nullable = false)
    private Integer idSucursalApertura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"IdTipoCuenta\"", nullable = false)
    private TipoCuentaAhorro tipoCuenta;

    @Column(name = "\"SaldoActual\"", nullable = false, precision = 18, scale = 2)
    private Double saldoActual;

    @Column(name = "\"SaldoDisponible\"", nullable = false, precision = 18, scale = 2)
    private Double saldoDisponible;

    @Column(name = "\"FechaApertura\"", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "\"FechaUltimaTransaccion\"")
    private LocalDateTime fechaUltimaTransaccion;

    @Column(name = "\"Estado\"", length = 10, nullable = false)
    private String estado; // ACTIVA / INACTIVA / CERRADA / BLOQUEADA
}
