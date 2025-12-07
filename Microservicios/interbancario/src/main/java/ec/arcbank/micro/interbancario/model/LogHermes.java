package ec.arcbank.micro.interbancario.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "LogHermes")
public class LogHermes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdLog")
    private Integer idLog;

    @Column(name = "IdOrden", nullable = false)
    private Integer idOrden;

    @Column(name = "MensajeEnviado", columnDefinition = "text")
    private String mensajeEnviado;

    @Column(name = "RespuestaRecibida", columnDefinition = "text")
    private String respuestaRecibida;

    @Column(name = "FechaEvento")
    private LocalDateTime fechaEvento = LocalDateTime.now();
}
