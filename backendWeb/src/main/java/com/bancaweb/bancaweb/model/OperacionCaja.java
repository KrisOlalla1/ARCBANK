package com.bancaweb.bancaweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "operacioncaja")
public class OperacionCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idoperacion")
    private Integer idOperacion;

    // 🔹 Relación hijo a padre (muchas operaciones → un usuario)
    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private UsuarioSistema usuario;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "idcuenta")
    private Integer idCuenta;

    @Column(name = "monto", precision = 18, scale = 2)
    private BigDecimal monto;

    @Column(name = "fechaoperacion", nullable = false)
    private LocalDateTime fechaOperacion = LocalDateTime.now();

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "estadooperacion", nullable = false, length = 15)
    private String estadoOperacion = "EXITOSA";

    // 🔹 Constructor vacío
    public OperacionCaja() {}

    // 🔹 Constructor solo para la clave primaria
    public OperacionCaja(Integer idOperacion) {
        this.idOperacion = idOperacion;
    }

    // 🔹 equals y hashCode solo por la PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperacionCaja)) return false;
        OperacionCaja that = (OperacionCaja) o;
        return Objects.equals(idOperacion, that.idOperacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOperacion);
    }

    // 🔹 toString con todas las propiedades
    @Override
    public String toString() {
        return "OperacionCaja{" +
                "idOperacion=" + idOperacion +
                ", usuario=" + (usuario != null ? usuario.getIdUsuario() : null) +
                ", tipo='" + tipo + '\'' +
                ", idCuenta=" + idCuenta +
                ", monto=" + monto +
                ", fechaOperacion=" + fechaOperacion +
                ", descripcion='" + descripcion + '\'' +
                ", estadoOperacion='" + estadoOperacion + '\'' +
                '}';
    }
}
