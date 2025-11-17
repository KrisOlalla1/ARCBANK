package com.bancaweb.bancaweb.service;

import com.bancaweb.bancaweb.model.OperacionCaja;
import com.bancaweb.bancaweb.model.UsuarioSistema;
import com.bancaweb.bancaweb.repository.OperacionCajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OperacionCajaService {

    @Autowired
    private OperacionCajaRepository operacionCajaRepository;

    /**
     * Consulta movimientos de un usuario entre dos fechas
     * Validando que el rango no sea mayor a 10 días
     * y que la fecha de inicio no sea más antigua de 5 meses
     */
    public List<OperacionCaja> obtenerMovimientosPorFecha(UsuarioSistema usuario, LocalDate desde, LocalDate hasta) {

        // Validar que el rango no sea mayor a 10 días
        long diasEntre = ChronoUnit.DAYS.between(desde, hasta);
        if (diasEntre > 10) {
            throw new IllegalArgumentException("Solo se permiten consultas de hasta 10 días.");
        }

        // Validar que no sea más de 5 meses atrás
        LocalDate haceCincoMeses = LocalDate.now().minusMonths(5);
        if (desde.isBefore(haceCincoMeses)) {
            throw new IllegalArgumentException("Solo puede consultar movimientos de los últimos 5 meses.");
        }

        LocalDateTime desdeHora = desde.atStartOfDay();
        LocalDateTime hastaHora = hasta.atTime(23, 59, 59);

        return operacionCajaRepository.findByUsuarioAndFechaOperacionBetween(usuario, desdeHora, hastaHora);
    }
}
