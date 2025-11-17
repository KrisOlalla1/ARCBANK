package com.arcbank.cbs.model.client;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Cliente")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TipoCliente", discriminatorType = DiscriminatorType.CHAR, length = 1)
@DiscriminatorValue("C")
public abstract class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCliente", nullable = false)
    private Integer idCliente;

    @Column(name = "TipoCliente", nullable = false, length = 1, insertable = false, updatable = false)
    private String tipoCliente;

    @Column(name = "TipoIdentificacion", nullable = false, length = 10)
    private String tipoIdentificacion;

    @Column(name = "Identificacion", nullable = false, length = 20)
    private String identificacion;

    @Column(name = "FechaRegistro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "Estado", nullable = false, length = 10)
    private String estado;

    protected Cliente() {
    }

    protected Cliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente that = (Cliente) o;
        return Objects.equals(idCliente, that.idCliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", tipoCliente='" + tipoCliente + '\'' +
                ", tipoIdentificacion='" + tipoIdentificacion + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", estado='" + estado + '\'' +
                '}';
    }
}