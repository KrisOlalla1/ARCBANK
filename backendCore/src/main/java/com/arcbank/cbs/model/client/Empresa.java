package com.arcbank.cbs.model.client;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Empresa")
@DiscriminatorValue("E")
@PrimaryKeyJoinColumn(name = "IdCliente")
public class Empresa extends Cliente {

    @Column(name = "RazonSocial", nullable = false, length = 150)
    private String razonSocial;

    @Column(name = "Ruc", nullable = false, length = 13, unique = true)
    private String ruc;

    @Column(name = "FechaConstitucion")
    private LocalDate fechaConstitucion;

    @Column(name = "DireccionPrincipal", length = 255)
    private String direccionPrincipal;

    public Empresa() {
        super();
    }

    public Empresa(Integer idCliente) {
        super(idCliente);
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "idCliente=" + getIdCliente() +
                ", razonSocial='" + razonSocial + '\'' +
                ", ruc='" + ruc + '\'' +
                ", fechaConstitucion=" + fechaConstitucion +
                ", direccionPrincipal='" + direccionPrincipal + '\'' +
                '}';
    }
}
