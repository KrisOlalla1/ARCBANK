package com.arcbank.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Cajero")
public class Cajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCajero")
    private Integer idCajero;

    @Column(name = "Usuario", nullable = false, length = 50, unique = true)
    private String usuario;

    @Column(name = "Clave", nullable = false, length = 100)
    private String clave;

    @Column(name = "NombreCompleto", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "IdSucursal", nullable = false)
    private Integer idSucursal;

    @Column(name = "Estado", nullable = false, length = 10)
    private String estado = "ACTIVO";
}