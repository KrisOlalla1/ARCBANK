package com.arcbank.cbs.model.client;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "EmpresaRepresentante")
@IdClass(EmpresaRepresentante.EmpresaRepresentanteId.class)
public class EmpresaRepresentante {

    @Id
    @Column(name = "IdEmpresa", nullable = false)
    private Integer idEmpresa;

    @Id
    @Column(name = "IdRepresentante", nullable = false)
    private Integer idRepresentante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdEmpresa", insertable = false, updatable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdRepresentante", insertable = false, updatable = false)
    private Persona representante;

    @Column(name = "Rol", length = 50)
    private String rol;

    @Column(name = "FechaDesignacion", nullable = false)
    private LocalDate fechaDesignacion;

    @Column(name = "FechaFinDesignacion")
    private LocalDate fechaFinDesignacion;

    @Column(name = "Estado", nullable = false, length = 10)
    private String estado;

    public EmpresaRepresentante() {
    }

    public EmpresaRepresentante(Integer idEmpresa, Integer idRepresentante) {
        this.idEmpresa = idEmpresa;
        this.idRepresentante = idRepresentante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpresaRepresentante that = (EmpresaRepresentante) o;
        return Objects.equals(idEmpresa, that.idEmpresa) &&
                Objects.equals(idRepresentante, that.idRepresentante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpresa, idRepresentante);
    }

    @Override
    public String toString() {
        return "EmpresaRepresentante{" +
                "idEmpresa=" + idEmpresa +
                ", idRepresentante=" + idRepresentante +
                ", rol='" + rol + '\'' +
                ", fechaDesignacion=" + fechaDesignacion +
                ", fechaFinDesignacion=" + fechaFinDesignacion +
                ", estado='" + estado + '\'' +
                '}';
    }

    @Getter
    @Setter
    public static class EmpresaRepresentanteId implements Serializable {
        private Integer idEmpresa;
        private Integer idRepresentante;

        public EmpresaRepresentanteId() {
        }

        public EmpresaRepresentanteId(Integer idEmpresa, Integer idRepresentante) {
            this.idEmpresa = idEmpresa;
            this.idRepresentante = idRepresentante;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EmpresaRepresentanteId that = (EmpresaRepresentanteId) o;
            return Objects.equals(idEmpresa, that.idEmpresa) &&
                    Objects.equals(idRepresentante, that.idRepresentante);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idEmpresa, idRepresentante);
        }
    }
}
