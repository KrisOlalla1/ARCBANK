package com.arcbank.cbs.model.geography;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un día festivo o feriado.
 * Puede ser nacional (aplica en todo el país) o local (aplica solo en una ubicación específica).
 */
@Entity
@Table(name = "Feriado", uniqueConstraints = @UniqueConstraint(columnNames = "Fecha"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feriado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdFeriado")
    private Integer idFeriado;

    @Column(name = "Fecha", nullable = false, unique = true)
    private LocalDate fecha;

    @Column(name = "Descripcion", nullable = false, length = 200)
    private String descripcion;

    /**
     * Indica si este feriado es nacional (aplica en todo el país)
     * o local (aplica solo en una ubicación específica).
     */
    @Column(name = "EsNacional", nullable = false)
    @Builder.Default
    private Boolean esNacional = false;

    @Column(name = "Estado", nullable = false, length = 10)
    @Builder.Default
    private String estado = "ACTIVA";

    /**
     * Relación N:1 con UbicacionGeografica.
     * Un feriado local está asociado a una ubicación geográfica específica.
     * NULL si el feriado es nacional.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUbicacion")
    private UbicacionGeografica ubicacionGeografica;

    @Override
    public String toString() {
        return "Feriado{" +
                "idFeriado=" + idFeriado +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", esNacional=" + esNacional +
                ", estado='" + estado + '\'' +
                '}';
    }
}
