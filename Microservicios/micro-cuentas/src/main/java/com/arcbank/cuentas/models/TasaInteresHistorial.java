package com.arcbank.cuentas.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "\"TasaInteresHistorial\"", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TasaInteresHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IdTasaHistorial\"")
    private Integer idTasaHistorial;

    // Hijo -> Padre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"IdTipoCuenta\"", nullable = false)
    private TipoCuentaAhorro tipoCuenta;

    @Column(name = "\"TasaInteresAnual\"", nullable = false, precision = 5, scale = 2)
    private Double tasaInteresAnual;

    @Column(name = "\"FechaInicioVigencia\"", nullable = false)
    private LocalDate fechaInicioVigencia;

    @Column(name = "\"FechaFinVigencia\"")
    private LocalDate fechaFinVigencia;

    @Column(name = "\"Observaciones\"", length = 255)
    private String observaciones;
}
