package com.arcbank.cajero.accounts.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import com.arcbank.cajero.admin.model.Cajero;

@Entity
@Table(name = "\"TransaccionCajero\"")
@Getter
@Setter
public class TransaccionCajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IdTransaccionCajero\"")
    private Integer idTransaccionCajero;

    @Column(name = "\"NumeroCuenta\"", nullable = false, length = 20)
    private String numeroCuenta;

    @Column(name = "\"Tipo\"", nullable = false, length = 10)
    private String tipo;

    @Column(name = "\"Monto\"", nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    @Column(name = "\"Balance\"", precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(name = "\"FechaHora\"", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "\"Descripcion\"", length = 255)
    private String descripcion;

    @Column(name = "\"Referencia\"", length = 50, unique = true)
    private String referencia;

    @ManyToOne
    @JoinColumn(name = "\"IdCajero\"", nullable = false)
    private Cajero cajero;

    @Column(name = "\"Estado\"", nullable = false, length = 20)
    private String estado;

    public TransaccionCajero() {}

    public TransaccionCajero(Integer idTransaccionCajero) {
        this.idTransaccionCajero = idTransaccionCajero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransaccionCajero)) return false;
        TransaccionCajero that = (TransaccionCajero) o;
        return Objects.equals(idTransaccionCajero, that.idTransaccionCajero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaccionCajero);
    }

    @Override
    public String toString() {
        return "TransaccionCajero{" +
                "idTransaccionCajero=" + idTransaccionCajero +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", monto=" + monto +
                ", balance=" + balance +
                ", fechaHora=" + fechaHora +
                ", descripcion='" + descripcion + '\'' +
                ", referencia='" + referencia + '\'' +
                ", cajero=" + cajero +
                ", estado='" + estado + '\'' +
                '}';
    }
}
