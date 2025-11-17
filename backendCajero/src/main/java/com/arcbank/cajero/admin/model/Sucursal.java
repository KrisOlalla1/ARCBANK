package com.arcbank.cajero.admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "\"Sucursal\"")
@Getter
@Setter
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IdSucursal\"")
    private Integer idSucursal;

    @Column(name = "\"CodigoUnico\"", nullable = false, unique = true, length = 10)
    private String codigoUnico;

    @Column(name = "\"Nombre\"", nullable = false, length = 100)
    private String nombre;

    @Column(name = "\"Direccion\"", length = 255)
    private String direccion;

    @Column(name = "\"Telefono\"", length = 20)
    private String telefono;

    @Column(name = "\"Estado\"", nullable = false, length = 10)
    private String estado;

    public Sucursal() {}

    public Sucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sucursal)) return false;
        Sucursal that = (Sucursal) o;
        return Objects.equals(idSucursal, that.idSucursal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSucursal);
    }

    @Override
    public String toString() {
        return "Sucursal{" +
                "idSucursal=" + idSucursal +
                ", codigoUnico='" + codigoUnico + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
