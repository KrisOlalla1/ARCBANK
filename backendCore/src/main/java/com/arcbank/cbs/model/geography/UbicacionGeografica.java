package com.arcbank.cbs.model.geography;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidad que representa una ubicación geográfica con estructura jerárquica.
 * Soporta 4 niveles: PAÍS → PROVINCIA → CANTÓN → PARROQUIA
 * La estructura jerárquica se implementa mediante auto-referencia (self-join).
 */
@Entity
@Table(name = "UbicacionGeografica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionGeografica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUbicacion")
    private Integer idUbicacion;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Tipo de ubicación: PAIS, PROVINCIA, CANTON, PARROQUIA
     */
    @Column(name = "Tipo", nullable = false, length = 20)
    private String tipo;

    /**
     * Referencia a la ubicación padre (para crear la jerarquía).
     * Null para ubicaciones raíz (ejemplo: País Ecuador).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUbicacionPadre")
    private UbicacionGeografica ubicacionPadre;

    // ...existing code...

    @Column(name = "Estado", nullable = false, length = 10)
    @Builder.Default
    private String estado = "ACTIVA";

    /**
     * Relación 1:N para ubicaciones hijo (self-referencing).
     * Una ubicación puede tener múltiples ubicaciones hijas.
     */
    @OneToMany(mappedBy = "ubicacionPadre", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<UbicacionGeografica> ubicacionesHijas = new ArrayList<>();

    @Override
    public String toString() {
        return "UbicacionGeografica{" +
                "idUbicacion=" + idUbicacion +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
