package com.arcbank.cbs.model.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidad que define los tipos de cuentas de ahorro disponibles.
 * Define parámetros como tasa de interés máxima y período de amortización.
 */
@Entity
@Table(name = "TipoCuentaAhorro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCuentaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipoCuenta")
    private Integer idTipoCuenta;

    @Column(name = "Nombre", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    /**
     * Tasa de interés máxima anual permitida para este tipo de cuenta.
     */
    @Column(name = "TasaInteresMaxima", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresMaxima;

    /**
     * Período de amortización de intereses: MENSUAL, TRIMESTRAL, ANUAL
     */
    @Column(name = "Amortizacion", nullable = false, length = 20)
    private String amortizacion;

    /**
     * Indica si este tipo de cuenta está activo para nuevas contrataciones.
     */
    @Column(name = "Activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // ...existing code...

    /**
     * Relación 1:N con TasaInteresHistorial.
     * Un tipo de cuenta puede tener múltiples entradas de tasa de interés históricas.
     */
    @OneToMany(mappedBy = "tipoCuentaAhorro", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<TasaInteresHistorial> tasasHistoriales = new ArrayList<>();

    @Override
    public String toString() {
        return "TipoCuentaAhorro{" +
                "idTipoCuenta=" + idTipoCuenta +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tasaInteresMaxima=" + tasaInteresMaxima +
                ", amortizacion='" + amortizacion + '\'' +
                ", activo=" + activo +
                '}';
    }
}
