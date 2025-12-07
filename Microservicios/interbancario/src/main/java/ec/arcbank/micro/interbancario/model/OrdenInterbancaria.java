package ec.arcbank.micro.interbancario.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "OrdenInterbancaria")
public class OrdenInterbancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdOrden")
    private Integer idOrden;

    @Column(name = "IdCuentaOrigen", nullable = false)
    private Integer idCuentaOrigen;

    @Column(name = "BancoDestinoId", nullable = false)
    private Integer bancoDestinoId;

    @Column(name = "CuentaDestino", nullable = false, length = 50)
    private String cuentaDestino;

    @Column(name = "NombreBeneficiario", length = 150)
    private String nombreBeneficiario;

    @Column(name = "IdentificacionBeneficiario", length = 20)
    private String identificacionBeneficiario;

    @Column(name = "Monto", nullable = false)
    private Double monto;

    @Column(name = "FechaRegistro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "FechaProceso")
    private LocalDateTime fechaProceso;

    @Column(name = "Estado", nullable = false)
    private String estado = "PENDIENTE";

    @Column(name = "ReferenciaBCE", length = 50)
    private String referenciaBCE;
}
