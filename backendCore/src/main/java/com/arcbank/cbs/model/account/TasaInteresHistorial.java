package com.arcbank.cbs.model.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "TasaInteresHistorial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TasaInteresHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTasaHistorial")
    private Integer idTasaHistorial;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IdTipoCuenta", nullable = false)
    private TipoCuentaAhorro tipoCuentaAhorro;


    @Column(name = "TasaInteresAnual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;


    @Column(name = "FechaInicioVigencia", nullable = false)
    private LocalDate fechaInicioVigencia;


    @Column(name = "FechaFinVigencia")
    private LocalDate fechaFinVigencia;


    @Column(name = "Observaciones", length = 500)
    private String observaciones;

    @Override
    public String toString() {
        return "TasaInteresHistorial{" +
                "idTasaHistorial=" + idTasaHistorial +
                ", tasaInteresAnual=" + tasaInteresAnual +
                ", fechaInicioVigencia=" + fechaInicioVigencia +
                ", fechaFinVigencia=" + fechaFinVigencia +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
