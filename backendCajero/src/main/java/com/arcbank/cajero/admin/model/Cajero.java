package com.arcbank.cajero.admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "\"Cajero\"")
@Getter
@Setter
public class Cajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IdCajero\"")
    private Integer idCajero;

    @Column(name = "\"Usuario\"", nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "\"Clave\"", nullable = false, length = 100)
    private String clave;

    @Column(name = "\"NombreCompleto\"", nullable = false, length = 150)
    private String nombreCompleto;

    @ManyToOne
    @JoinColumn(name = "\"IdSucursal\"", nullable = false)
    private Sucursal sucursal;

    @Column(name = "\"Estado\"", nullable = false, length = 10)
    private String estado;

    public Cajero() {}

    public Cajero(Integer idCajero) {
        this.idCajero = idCajero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cajero)) return false;
        Cajero cajero = (Cajero) o;
        return Objects.equals(idCajero, cajero.idCajero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCajero);
    }

    @Override
    public String toString() {
        return "Cajero{" +
                "idCajero=" + idCajero +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", sucursal=" + sucursal +
                ", estado='" + estado + '\'' +
                '}';
    }
}
