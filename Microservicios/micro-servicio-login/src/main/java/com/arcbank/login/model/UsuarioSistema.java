package com.arcbank.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "UsuarioSistema")
public class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Integer idUsuario;

    @Column(name = "NombreUsuario", nullable = false, length = 50, unique = true)
    private String nombreUsuario;

    @Column(name = "ClaveHash", nullable = false, length = 255)
    private String claveHash;

    @Column(name = "IdSucursal")
    private Integer idSucursal;

    @Column(name = "FechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "UltimoAcceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "Estado", nullable = false, length = 15)
    private String estado = "ACTIVO";
}