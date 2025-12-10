package com.arcbank.cuentas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Objects;

/*
 touch src/main/java/com/example/cuentas/entity/TasaInteresHistorial.java
*/
@Entity
@Table(name = "TasaInteresHistorial", schema = "public")
@Getter
@Setter
public class TasaInteresHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTasaHistorial")
    private Integer idTasaHistorial;

    // Relacion hijo -> padre con TipoCuentaAhorro
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IdTipoCuenta", nullable = false, foreignKey = @ForeignKey(name = "fk_tasa_tipo_cuenta"))
    private TipoCuentaAhorro tipoCuenta;

    @Column(name = "TasaInteresAnual", precision = 5, scale = 2, nullable = false)
    private java.math.BigDecimal tasaInteresAnual;

    @Column(name = "FechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FechaFin")
    private LocalDate fechaFin;

    // Constructor vacío
    public TasaInteresHistorial() { }

    // Constructor solo para la clave primaria
    public TasaInteresHistorial(Integer idTasaHistorial) {
        this.idTasaHistorial = idTasaHistorial;
    }

    // equals y hashCode comparando solo la clave primaria
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TasaInteresHistorial that = (TasaInteresHistorial) o;
        return Objects.equals(idTasaHistorial, that.idTasaHistorial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTasaHistorial);
    }

    // toString con todas las propiedades
    @Override
    public String toString() {
        return "TasaInteresHistorial{" +
                "idTasaHistorial=" + idTasaHistorial +
                ", tipoCuentaId=" + (tipoCuenta != null ? tipoCuenta.getIdTipoCuenta() : null) +
                ", tasaInteresAnual=" + tasaInteresAnual +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}