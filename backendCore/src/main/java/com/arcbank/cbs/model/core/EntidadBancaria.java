package com.arcbank.cbs.model.core;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una institución bancaria (matriz, grupo).
 * Una entidad bancaria puede tener múltiples sucursales.
 */
@Entity
@Table(name = "EntidadBancaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntidadBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEntidad")
    private Integer idEntidad;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Ruc", nullable = false, length = 13, unique = true)
    private String ruc;

    @Column(name = "Estado", nullable = false, length = 10)
    @Builder.Default
    private String estado = "ACTIVA";

    // ...existing code...
    @Override
    public String toString() {
        return "EntidadBancaria{" +
                "idEntidad=" + idEntidad +
                ", nombre='" + nombre + '\'' +
                ", ruc='" + ruc + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
