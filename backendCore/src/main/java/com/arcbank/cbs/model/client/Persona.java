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
@Table(name = "Persona")
@DiscriminatorValue("P")
@PrimaryKeyJoinColumn(name = "IdCliente")
public class Persona extends Cliente {

    @Column(name = "Nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "Apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "Identificacion", nullable = false, length = 10, unique = true)
    private String identificacionPersona;

    @Column(name = "FechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "DireccionPrincipal", length = 255)
    private String direccionPrincipal;

    public Persona() {
        super();
    }

    public Persona(Integer idCliente) {
        super(idCliente);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "idCliente=" + getIdCliente() +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", identificacion='" + identificacionPersona + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", direccionPrincipal='" + direccionPrincipal + '\'' +
                '}';
    }
}
