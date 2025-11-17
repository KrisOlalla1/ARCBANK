package com.arcbank.cbs.model.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.arcbank.cbs.model.core.EntidadBancaria;
import com.arcbank.cbs.model.geography.UbicacionGeografica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Sucursal")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSucursal", nullable = false)
    private Integer idSucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdEntidad", nullable = false)
    private EntidadBancaria entidadBancaria;

    @Column(name = "IdEntidad", insertable = false, updatable = false, nullable = false)
    private Integer idEntidad;

    @Column(name = "CodigoUnico", nullable = false, length = 10, unique = true)
    private String codigoUnico;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Direccion", length = 255)
    private String direccion;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Latitud", precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(name = "Longitud", precision = 9, scale = 6)
    private BigDecimal longitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUbicacion", nullable = false)
    private UbicacionGeografica ubicacionGeografica;

    @Column(name = "IdUbicacion", insertable = false, updatable = false, nullable = false)
    private Integer idUbicacion;

    @Column(name = "Estado", nullable = false, length = 10)
    private String estado;

    @Column(name = "FechaApertura", nullable = false)
    private LocalDate fechaApertura;

    public Sucursal() {
    }

    public Sucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
                ", idEntidad=" + idEntidad +
                ", codigoUnico='" + codigoUnico + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", idUbicacion=" + idUbicacion +
                ", estado='" + estado + '\'' +
                ", fechaApertura=" + fechaApertura +
                '}';
    }
}
