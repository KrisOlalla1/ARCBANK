package ec.arcbank.micro.interbancario.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler desactivado por ahora.
 * En el futuro podrá ejecutar reenvíos o sincronizaciones.
 */
@Component
public class OrdenScheduler {

    // Ejecución cada 1 minuto (DESACTIVADO lógicamente, solo estructura)
    @Scheduled(fixedRate = 60000)
    public void procesarOrdenesPendientes() {
        // Por ahora no implementado
        // Cuando se active:
        // 1. Buscar órdenes PENDIENTE
        // 2. Llamar al servicio asincrónico para procesarlas
    }
}
