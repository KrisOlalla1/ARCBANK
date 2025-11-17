package com.arcbank.cbs.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.account.TasaInteresHistorial;

/**
 * Repositorio para la entidad TasaInteresHistorial.
 */
@Repository
public interface TasaInteresHistorialRepository extends JpaRepository<TasaInteresHistorial, Integer> {
    /**
     * Busca todas las tasas históricas de un tipo de cuenta.
     */
    List<TasaInteresHistorial> findByTipoCuentaAhorroIdTipoCuenta(Integer idTipoCuenta);

    /**
     * Busca la tasa vigente (actual) de un tipo de cuenta.
     * La tasa vigente es aquella cuya FechaFinVigencia es NULL.
     */
    @Query("SELECT th FROM TasaInteresHistorial th " +
           "WHERE th.tipoCuentaAhorro.idTipoCuenta = :idTipoCuenta " +
           "AND th.fechaFinVigencia IS NULL")
    Optional<TasaInteresHistorial> findVigenteByIdTipoCuenta(@Param("idTipoCuenta") Integer idTipoCuenta);

    /**
     * Busca la tasa histórica vigente en una fecha específica.
     */
    @Query("SELECT th FROM TasaInteresHistorial th " +
           "WHERE th.tipoCuentaAhorro.idTipoCuenta = :idTipoCuenta " +
           "AND th.fechaInicioVigencia <= :fecha " +
           "AND (th.fechaFinVigencia IS NULL OR th.fechaFinVigencia >= :fecha)")
    Optional<TasaInteresHistorial> findByIdTipoCuentaAndFecha(
            @Param("idTipoCuenta") Integer idTipoCuenta,
            @Param("fecha") LocalDate fecha
    );

    /**
     * Busca cambios de tasa en un rango de fechas.
     */
    List<TasaInteresHistorial> findByFechaInicioVigenciaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
