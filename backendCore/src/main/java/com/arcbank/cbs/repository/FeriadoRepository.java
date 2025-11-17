package com.arcbank.cbs.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.geography.Feriado;

/**
 * Repositorio para la entidad Feriado.
 */
@Repository
public interface FeriadoRepository extends JpaRepository<Feriado, Integer> {
    /**
     * Busca un feriado por su fecha.
     */
    Optional<Feriado> findByFecha(LocalDate fecha);

    /**
     * Busca feriados nacionales (aplican en todo el país).
     */
    List<Feriado> findByEsNacionalTrue();

    /**
     * Busca feriados locales de una ubicación específica.
     */
    List<Feriado> findByUbicacionGeograficaIdUbicacion(Integer idUbicacion);

    /**
     * Busca feriados vigentes (estado = ACTIVA) en un rango de fechas.
     */
    List<Feriado> findByFechaBetweenAndEstado(LocalDate fechaInicio, LocalDate fechaFin, String estado);

    /**
     * Busca feriados por fecha y ubicación (nacional o local).
     */
    @Query("SELECT f FROM Feriado f WHERE f.fecha = :fecha " +
           "AND (f.esNacional = true OR f.ubicacionGeografica.idUbicacion = :idUbicacion)")
    List<Feriado> findByFechaAndUbicacion(
            @Param("fecha") LocalDate fecha,
            @Param("idUbicacion") Integer idUbicacion
    );

    /**
     * Verifica si existe un feriado para una fecha determinada.
     */
    boolean existsByFecha(LocalDate fecha);
}

