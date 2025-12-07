package ec.arcbank.micro.interbancario.service;

import ec.arcbank.micro.interbancario.model.OrdenInterbancaria;
import ec.arcbank.micro.interbancario.repository.OrdenInterbancariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcesadorAsincronoService {

    private final HermesService hermesService;
    private final OrdenInterbancariaRepository ordenRepo;

    @Async
    public void procesarOrden(OrdenInterbancaria orden) {

        var respuesta = hermesService.enviarOrden(orden);

        orden.setFechaProceso(LocalDateTime.now());

        if ("APROBADA".equals(respuesta.getEstado())) {
            orden.setEstado("ACREDITADA");
            orden.setReferenciaBCE(respuesta.getReferenciaBCE());
        } else {
            orden.setEstado("RECHAZADA");
        }

        ordenRepo.save(orden);
    }
}
