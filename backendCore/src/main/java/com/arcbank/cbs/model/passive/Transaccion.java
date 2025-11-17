package com.arcbank.cbs.model.passive;

import com.arcbank.cbs.model.admin.Sucursal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Transaccion")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTransaccion", nullable = false)
    private Integer idTransaccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCuenta", nullable = false)
    private CuentaAhorro cuentaAhorro;

    @Column(name = "IdCuenta", insertable = false, updatable = false, nullable = false)
    private Integer idCuenta;

    @Column(name = "Tipo", nullable = false, length = 15)
    private String tipo;

    @Column(name = "Monto", nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    @Column(name = "Balance", precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(name = "FechaHora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "Descripcion", length = 255)
    private String descripcion;

    @Column(name = "Referencia", length = 50)
    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdSucursalTx")
    private Sucursal sucursalTx;

    @Column(name = "IdSucursalTx", insertable = false, updatable = false)
    private Integer idSucursalTx;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTransaccionReversa")
    private Transaccion transaccionReversa;

    @Column(name = "IdTransaccionReversa", insertable = false, updatable = false)
    private Integer idTransaccionReversa;

    public Transaccion() {
    }

    public Transaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaccion that = (Transaccion) o;
        return Objects.equals(idTransaccion, that.idTransaccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaccion);
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "idTransaccion=" + idTransaccion +
                ", idCuenta=" + idCuenta +
                ", tipo='" + tipo + '\'' +
                ", monto=" + monto +
                ", balance=" + balance +
                ", fechaHora=" + fechaHora +
                ", descripcion='" + descripcion + '\'' +
                ", referencia='" + referencia + '\'' +
                ", idSucursalTx=" + idSucursalTx +
                ", estado='" + estado + '\'' +
                ", idTransaccionReversa=" + idTransaccionReversa +
                '}';
    }
}
