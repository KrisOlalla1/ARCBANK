package com.arcbank.cuentas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

/*
 touch src/main/java/com/example/cuentas/entity/TipoCuentaAhorro.java
*/
@Entity
@Table(name = "TipoCuentaAhorro", schema = "public")
@Getter
@Setter
public class TipoCuentaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipoCuenta")
    private Integer idTipoCuenta;

    @Column(name = "Nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(name = "Descripcion", length = 255)
    private String descripcion;

    @Column(name = "TasaInteresMaxima", precision = 5, scale = 2, nullable = false)
    private java.math.BigDecimal tasaInteresMaxima;

    @Column(name = "Amortizacion", length = 20, nullable = false)
    private String amortizacion;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    // Constructor vacío
    public TipoCuentaAhorro() { }

    // Constructor solo para la clave primaria
    public TipoCuentaAhorro(Integer idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    // equals y hashCode comparando solo la clave primaria
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TipoCuentaAhorro that = (TipoCuentaAhorro) o;
        return Objects.equals(idTipoCuenta, that.idTipoCuenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoCuenta);
    }

    // toString con todas las propiedades
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
