package com.arcbank.cbs.model.passive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.arcbank.cbs.model.account.TipoCuentaAhorro;
import com.arcbank.cbs.model.admin.Sucursal;
import com.arcbank.cbs.model.client.Cliente;

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
@Table(name = "CuentaAhorro")
public class CuentaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCuenta", nullable = false)
    private Integer idCuenta;

    @Column(name = "NumeroCuenta", nullable = false, length = 20, unique = true)
    private String numeroCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCliente", nullable = false)
    private Cliente cliente;

    @Column(name = "IdCliente", insertable = false, updatable = false, nullable = false)
    private Integer idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipoCuenta", nullable = false)
    private TipoCuentaAhorro tipoCuentaAhorro;

    @Column(name = "IdTipoCuenta", insertable = false, updatable = false, nullable = false)
    private Integer idTipoCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdSucursalApertura", nullable = false)
    private Sucursal sucursalApertura;

    @Column(name = "IdSucursalApertura", insertable = false, updatable = false, nullable = false)
    private Integer idSucursalApertura;

    @Column(name = "SaldoActual", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoActual;

    @Column(name = "SaldoDisponible", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(name = "FechaApertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "FechaUltimaTransaccion")
    private LocalDateTime fechaUltimaTransaccion;

    @Column(name = "Estado", nullable = false, length = 10)
    private String estado;

    public CuentaAhorro() {
    }

    public CuentaAhorro(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CuentaAhorro that = (CuentaAhorro) o;
        return Objects.equals(idCuenta, that.idCuenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCuenta);
    }

    @Override
    public String toString() {
        return "CuentaAhorro{" +
                "idCuenta=" + idCuenta +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", idCliente=" + idCliente +
                ", idTipoCuenta=" + idTipoCuenta +
                ", idSucursalApertura=" + idSucursalApertura +
                ", saldoActual=" + saldoActual +
                ", saldoDisponible=" + saldoDisponible +
                ", fechaApertura=" + fechaApertura +
                ", fechaUltimaTransaccion=" + fechaUltimaTransaccion +
                ", estado='" + estado + '\'' +
                '}';
    }
}
