package com.arcbank.cbs.model.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que mantiene el historial de tasas de interés para cada tipo de cuenta.
 * Permite auditar cambios en tasas y conocer la tasa vigente.
 */
@Entity
@Table(name = "TasaInteresHistorial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TasaInteresHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTasaHistorial")
    private Integer idTasaHistorial;

    /**
     * Relación N:1 con TipoCuentaAhorro.
     * Muchas entradas de tasa histórica pueden pertenecer a un mismo tipo de cuenta.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IdTipoCuenta", nullable = false)
    private TipoCuentaAhorro tipoCuentaAhorro;

    /**
     * Tasa de interés anual en vigencia durante el período.
     */
    @Column(name = "TasaInteresAnual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    /**
     * Fecha de inicio de vigencia de esta tasa.
     */
    @Column(name = "FechaInicioVigencia", nullable = false)
    private LocalDate fechaInicioVigencia;

    /**
     * Fecha de fin de vigencia de esta tasa.
     * NULL indica que la tasa está vigente actualmente.
     */
    @Column(name = "FechaFinVigencia")
    private LocalDate fechaFinVigencia;

    /**
     * Observaciones adicionales sobre el cambio de tasa.
     */
    @Column(name = "Observaciones", length = 500)
    private String observaciones;

    @Override
    public String toString() {
        return "TasaInteresHistorial{" +
                "idTasaHistorial=" + idTasaHistorial +
                ", tasaInteresAnual=" + tasaInteresAnual +
                ", fechaInicioVigencia=" + fechaInicioVigencia +
                ", fechaFinVigencia=" + fechaFinVigencia +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
