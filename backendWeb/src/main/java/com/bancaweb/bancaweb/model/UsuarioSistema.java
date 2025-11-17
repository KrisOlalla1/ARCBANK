package com.bancaweb.bancaweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "usuariosistema")
public class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "nombreusuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(name = "clavehash", nullable = false, length = 255)
    private String claveHash;

    @Column(name = "idsucursal")
    private Integer idSucursal;

    @Column(name = "identificacion", length = 20)
    private String identificacion;

    @Column(name = "fechacreacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "ultimoacceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    // 🔹 Constructor vacío (sin Lombok)
    public UsuarioSistema() {}

    // 🔹 Constructor solo para la clave primaria
    public UsuarioSistema(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    // 🔹 equals y hashCode solo por la PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioSistema)) return false;
        UsuarioSistema that = (UsuarioSistema) o;
        return Objects.equals(idUsuario, that.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }

    // 🔹 toString con todas las propiedades
    @Override
    public String toString() {
        return "UsuarioSistema{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", claveHash='" + claveHash + '\'' +
                ", idSucursal=" + idSucursal +
                ", fechaCreacion=" + fechaCreacion +
                ", ultimoAcceso=" + ultimoAcceso +
                ", estado='" + estado + '\'' +
                '}';
    }
}
