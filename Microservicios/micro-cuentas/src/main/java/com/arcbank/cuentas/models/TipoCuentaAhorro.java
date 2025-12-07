package com.arcbank.cuentas.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "\"TipoCuentaAhorro\"", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCuentaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IdTipoCuenta\"")
    private Integer idTipoCuenta;

    @Column(name = "\"Nombre\"", length = 30, nullable = false, unique = true)
    private String nombre;

    @Column(name = "\"Descripcion\"", length = 255)
    private String descripcion;

    @Column(name = "\"TasaInteresMaxima\"", nullable = false, precision = 5, scale = 2)
    private Double tasaInteresMaxima;

    @Column(name = "\"Amortizacion\"", length = 30)
    private String amortizacion;

    @Column(name = "\"Activo\"", nullable = false)
    private Boolean activo = true;

    // HIJOS: TasaInteresHistorial
    @OneToMany(mappedBy = "tipoCuenta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TasaInteresHistorial> historialTasas;

    // HIJOS: CuentaAhorro
    @OneToMany(mappedBy = "tipoCuenta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CuentaAhorro> cuentas;
}
