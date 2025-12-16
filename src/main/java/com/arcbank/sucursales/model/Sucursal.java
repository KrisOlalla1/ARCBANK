package com.arcbank.sucursales.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Document(collection = "sucursales")
@Getter
@Setter
public class Sucursal {

    @Id
    private String idSucursal; // ObjectId Mongo

    private String codigoUnico;
    private String nombre;
    private String direccion;
    private String telefono;

    private Double latitud;
    private Double longitud;

    private String estado;
    private LocalDate fechaApertura;

    // Subdocumentos embebidos
    private EntidadBancaria entidadBancaria;
    private Ubicacion ubicacion;

    // Constructor vacío
    public Sucursal() { }

    // Constructor solo PK
    public Sucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    // equals y hashCode SOLO por PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sucursal sucursal = (Sucursal) o;
        return Objects.equals(idSucursal, sucursal.idSucursal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSucursal);
    }

    @Override
    public String toString() {
        return "Sucursal{" +
                "idSucursal='" + idSucursal + '\'' +
                ", codigoUnico='" + codigoUnico + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", estado='" + estado + '\'' +
                ", fechaApertura=" + fechaApertura +
                ", entidadBancaria=" + entidadBancaria +
                ", ubicacion=" + ubicacion +
                '}';
    }
}
